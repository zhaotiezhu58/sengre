/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.minghui.callback.exception;

import cn.hutool.core.collection.CollUtil;
import com.minghui.commons.utils.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class GlobleExceptionController {

	/**
	 * validation 捕获 MethodArgumentNotValidException 异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public String serviceHandle(MethodArgumentNotValidException e) {
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		if (CollUtil.isNotEmpty(allErrors)) {
			String message = MsgUtil.get(allErrors.get(0).getDefaultMessage());
			if (StringUtils.isBlank(message)) {
				return allErrors.get(0).getDefaultMessage();
			}
		}
		return "error";
	}
	/**
	 * validation 捕获 BindException 异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public String handleBindException(BindException ex) {
		ex.printStackTrace();
		List<ObjectError> allErrors = ex.getAllErrors();
		if (CollUtil.isNotEmpty(allErrors)) {
			String message = MsgUtil.get(allErrors.get(0).getDefaultMessage());
			if (StringUtils.isBlank(message)) {
				return allErrors.get(0).getDefaultMessage();
			}
			return message;
		}
		return "error";
	}
	/**
	 * validation拦截 ConstraintViolationException 异常
	 * 用于单个异常校验
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleBindException(ConstraintViolationException ex) {
		ex.printStackTrace();
		return "error";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e){
		e.printStackTrace();
		return "error";
	}
}
