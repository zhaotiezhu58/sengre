package com.minghui.commons.constants;

import java.util.HashMap;
import java.util.Map;

public class LangConstant {

    public static final Map<String, String> LANG_ES_MAP = new HashMap<>();
    public static final Map<String, String> LANG_EN_MAP = new HashMap<>();

    static {
        LANG_ES_MAP.put("system.failure", "Operación fallida.");
        LANG_ES_MAP.put("system.success", "Operación exitosa.");
        LANG_ES_MAP.put("system.param.err", "Error de parámetros.");
        LANG_ES_MAP.put("system.not.login", "Por favor, conéctese.");
        LANG_ES_MAP.put("system.token.invalid", "Vuelva a iniciar sesión.");
        LANG_ES_MAP.put("system.phone.not.reg", "Nombre de usuario o contrase?a incorrectos.");
        LANG_ES_MAP.put("system.phone.validerror", "Error de número de teléfono válido!");
        LANG_ES_MAP.put("system.account.enable", "Nombre de usuario o contrase?a incorrectos.");
        LANG_ES_MAP.put("system.sms.day.max", "Envías mensajes de texto para llegar al límite hoy.");
        LANG_ES_MAP.put("system.sms.send.success", "Enviado con éxito.");
        LANG_ES_MAP.put("system.sms.send.error", "Enviado sin éxito.");
        LANG_ES_MAP.put("system.user.login.pwd.limit", "El número de errores de introducción de la contrase?a supera el límite máximo y la cuenta se congela durante 24 horas.");
        LANG_ES_MAP.put("system.user.login.pwd.error", "Nombre de usuario o contrase?a incorrectos.");
        LANG_ES_MAP.put("system.user.login.pwd.success", "Bienvenido de nuevo!");
        LANG_ES_MAP.put("system.pwd.different", "El nombre de usuario o la contrase?a no son correctos.");
        LANG_ES_MAP.put("system.order.account.balance", "Equilibrio insuficiente.");
        LANG_ES_MAP.put("system.pwd.old", "La nueva contrase?a no puede ser la misma que la actual.");
        LANG_ES_MAP.put("system.pwd.sms", "Por favor, introduzca el código de verificación .");
        LANG_ES_MAP.put("system.pwd.sm.error", "Código incorrecto.");
        LANG_ES_MAP.put("system.pwd.sm.success", "Operación exitosa.");
        LANG_ES_MAP.put("system.register.has", "El número de teléfono móvil ha sido registrado.");
        LANG_ES_MAP.put("system.register.yqm.error", "Código de invitación inválido.");
        LANG_ES_MAP.put("system.register.success", "Regístrese con éxito.");
        LANG_ES_MAP.put("system.withdraw.choose.bank", "Por favor, vincule la dirección!");
        LANG_ES_MAP.put("validation.forget.code.error", "Por favor, introduzca el código de verificación.");
        LANG_ES_MAP.put("validation.forget.pwd.error", "Por favor, introduzca una contrase?a con una combinación de números y letras de 6 a 12 dígitos.");
        LANG_ES_MAP.put("validation.bank.withdraw.error", "Contrase?a de retirada incorrecta.");
        LANG_ES_MAP.put("validation.withdraw.pwd.error", "Por favor, introduzca una contrase?a de retirada con 4 de números.");
        LANG_ES_MAP.put("pg.system.logout.error", "Se ha cerrado la sesión con éxito.");
        LANG_ES_MAP.put("pg.system.withdraw.success", "Enviado con éxito, espere pacientemente la aprobación.");
        LANG_ES_MAP.put("system.withdraw.maxcount", "Se ha alcanzado el límite de retiro diario.");
        LANG_ES_MAP.put("system.withdraw.nocheck", "Todavía tiene órdenes de retiro sin revisar.");
        LANG_ES_MAP.put("system.withdraw.minamount", "El importe mínimo es de {}.");

        LANG_ES_MAP.put("system.withdraw.noauth", "Póngase en contacto con el administrador.");

        // 活动相关
        LANG_ES_MAP.put("system.activity.betweentime", "Tiempo de actividad.");
        LANG_ES_MAP.put("system.activity.nocount", "No se puede participar en el evento.");


        LANG_EN_MAP.put("system.failure", "Operation failed.");
        LANG_EN_MAP.put("system.success", "Operation succeeded.");
        LANG_EN_MAP.put("system.param.err", "params error.");
        LANG_EN_MAP.put("system.not.login", "Please log in.");
        LANG_EN_MAP.put("system.token.invalid", "Please log in again.");
        LANG_EN_MAP.put("system.phone.not.reg", "Incorrect username or password.");
        LANG_EN_MAP.put("system.phone.validerror", "valid phonenumber error!");
        LANG_EN_MAP.put("system.account.enable", "Incorrect username or password.");
        LANG_EN_MAP.put("system.sms.day.max", "You send text messages to reach the limit today.");
        LANG_EN_MAP.put("system.sms.send.success", "Successfully sent.");
        LANG_EN_MAP.put("system.sms.send.error", "Unsuccessfully sent.");
        LANG_EN_MAP.put("system.user.login.pwd.limit", "The number of password input errors exceeds the upper limit, and the account is frozen for 24 hours.");
        LANG_EN_MAP.put("system.user.login.pwd.error", "Incorrect username or password.");
        LANG_EN_MAP.put("system.user.login.pwd.success", "Welcome back!");
        LANG_EN_MAP.put("system.pwd.different", "The passwordncorrect username or password.");
        LANG_EN_MAP.put("system.order.account.balance", "Insufficient balance.");
        LANG_EN_MAP.put("system.pwd.old", "The new password cannot be the same as the current password.");
        LANG_EN_MAP.put("system.pwd.sms", "Please enter the verification code .");
        LANG_EN_MAP.put("system.pwd.sm.error", "Incorrect code.");
        LANG_EN_MAP.put("system.pwd.sm.success", "Operation succeeded.");
        LANG_EN_MAP.put("system.register.has", "The mobile phone number has been registered.");
        LANG_EN_MAP.put("system.register.yqm.error", "Invalid invitation code.");
        LANG_EN_MAP.put("system.register.success", "Register successfully.");
        LANG_EN_MAP.put("system.withdraw.choose.bank", "Please bind address!");
        LANG_EN_MAP.put("validation.forget.code.error", "Please enter the verification code.");
        LANG_EN_MAP.put("validation.forget.pwd.error", "Please enter a password with 6-12 digits of numbers / letters combination.");
        LANG_EN_MAP.put("validation.bank.withdraw.error", "Incorrect withdrawal password.");
        LANG_EN_MAP.put("validation.withdraw.pwd.error", "Please enter a withdrawal password with 4 of numbers.");
        LANG_EN_MAP.put("pg.system.logout.error", "Logged out successfully.");
        LANG_EN_MAP.put("pg.system.withdraw.success", "Submitted successfully, please wait patiently for approval.");
        LANG_EN_MAP.put("system.withdraw.maxcount", "The daily withdrawal limit has been reached.");
        LANG_EN_MAP.put("system.withdraw.nocheck", "You still have unreviewed withdrawal orders.");
        LANG_EN_MAP.put("system.withdraw.minamount", "The minimum amount is {}.");

        LANG_EN_MAP.put("system.activity.betweentime", "Activity time.");
        LANG_EN_MAP.put("system.activity.nocount", "Unable to participate in the event.");

        LANG_EN_MAP.put("system.withdraw.noauth", "Please contact the administrator.");
    }
}
