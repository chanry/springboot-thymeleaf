package com.trs.gallery.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.trs.gallery.util.ParameterCheckUtil;

import net.sf.json.JSONArray;

/**
 * 校验请求参数
 * 
 * @Description: TODO(用一句话描述这个类的详细作用)
 * @author chenli
 * @date 2016年5月18日
 * @version v1.1
 */
@Aspect
@Configuration
public class ParameterVerifyAop
{
	private static Logger LOG = LoggerFactory.getLogger(ParameterVerifyAop.class);
	/**
	 * 类或方法名跳过特殊字符检验的正则表达式
	 */
	private String targetPattern = "";
	
	/**
	 * 参数级别跳过特殊字符检验的正则表达式
	 */
	private String paramTargetPattern = "";
	
	/**
	 * 参数级别跳过json检验的正则表达式
	 */
	private String jsonIgnorePattern = "";
	
	
	public String getTargetPattern()
	{
		return this.targetPattern;
	}

	public void setTargetPattern(String targetPattern)
	{
		this.targetPattern = targetPattern;
	}

	public String getParamTargetPattern()
	{
		return paramTargetPattern;
	}

	public void setParamTargetPattern(String paramTargetPattern)
	{
		this.paramTargetPattern = paramTargetPattern;
	}
	
	public String getJsonIgnorePattern()
	{
		return jsonIgnorePattern;
	}

	public void setJsonIgnorePattern(String jsonIgnorePattern)
	{
		this.jsonIgnorePattern = jsonIgnorePattern;
	}
	
	//匹配com.trs.gallery.controller.front包及其子包下的所有类的所有方法  
    @Pointcut("execution(* com.trs.gallery.controller.front..*.*(..))")  
    public void executeService(){  
  
    }  
    
    @Before("executeService()") 
	public void parameterVerify(JoinPoint joinPoint)
	{
		Object[] objArr = joinPoint.getArgs();
		LOG.debug("请求目标方法为：{}",
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		List<String> paraList = new ArrayList<String>();
		List<String> noSpecialCharVerifyList = new ArrayList<String>();
		List<String> noJsonVerifyList = new ArrayList<String>();
		boolean hasRequest = false;
		
		for (int i = 0; i < objArr.length; i++)
		{
			if (objArr[i] instanceof HttpServletRequest)
			{
				hasRequest = true;
				break;
			}
		}
		
		for (int i = 0; i < objArr.length; i++)
		{
			if (hasRequest)
			{
				if (objArr[i] instanceof HttpServletRequest)
				{
					HttpServletRequest request = (HttpServletRequest) objArr[i];
					Map<String, String[]> parameterMap = request.getParameterMap();
					List<String> reqParaList = null;
					Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
					for(Map.Entry<String, String[]> entry: entrySet){
						reqParaList =  Arrays.asList(entry.getValue());
						if (CollectionUtils.isNotEmpty(reqParaList) && reqParaList.size() == 1)
						{
							if (Pattern.matches(this.paramTargetPattern, entry.getKey()))
							{
								noSpecialCharVerifyList.add(reqParaList.get(0));
							}
							else if (Pattern.matches(this.jsonIgnorePattern, entry.getKey()))
							{
								noJsonVerifyList.add(reqParaList.get(0));
							}
							else
							{
								paraList.add(reqParaList.get(0));
							}
						}
						reqParaList = null;
					}
					break;
				}
			}
			else
			{
				if (!(objArr[i] instanceof String))
					continue;
				paraList.add(objArr[i].toString());
			}
		}

		LOG.debug("请求参数：{}", JSONArray.fromObject(paraList).toString());
		if (CollectionUtils.isNotEmpty(paraList))
		{
			if ((Pattern.matches(this.targetPattern, joinPoint.getTarget().toString()))
					|| (Pattern.matches(this.targetPattern, joinPoint.getSignature().getName())))
			{
				ParameterCheckUtil.verifyInput(true, true, (String[]) paraList.toArray(new String[paraList.size()]));
			}
			else
			{
				ParameterCheckUtil.verifyInput(true, (String[]) paraList.toArray(new String[paraList.size()]));
			}
		}
		//不进行特殊字符校验参数
		if (CollectionUtils.isNotEmpty(noSpecialCharVerifyList))
		{
			LOG.debug("请求不校验特殊字符参数：{}", JSONArray.fromObject(noSpecialCharVerifyList));
			ParameterCheckUtil.verifyUrl(true, (String[]) noSpecialCharVerifyList.toArray(new String[noSpecialCharVerifyList.size()]));
			ParameterCheckUtil.verifyInput(true, true, (String[]) noSpecialCharVerifyList.toArray(new String[noSpecialCharVerifyList.size()]));
		}
		
		//不进行json校验参数
		if (CollectionUtils.isNotEmpty(noJsonVerifyList))
		{
			LOG.debug("请求不校验json参数：{}", JSONArray.fromObject(noJsonVerifyList));
			ParameterCheckUtil.verifyJson(true, (String[]) noJsonVerifyList.toArray(new String[noJsonVerifyList.size()]));
		}
	}
}
