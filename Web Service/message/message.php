<?php
	
	require_once('message_code.php');
	
class Message{

	const TAG_CODE = "code";
	const TAG_MESSAGE = "message";
	
	function __construct(){
		
	}	
	
	function __destruct(){
	
	}
	
	static function validLogin(){
		return array(self::TAG_CODE => VALID_CLIENT_LOGIN,
					self::TAG_MESSAGE => VALID_CLIENT_LOGIN_MESSAGE);
	}
	
	static function invalidLogin(){
		return array(self::TAG_CODE => INVALID_CLIENT_LOGIN,
					self::TAG_MESSAGE => INVALID_CLIENT_LOGIN_MESSAGE);
	}
	
	static function validLogout(){
		return array(self::TAG_CODE => VALID_CLIENT_LOGOUT,
					self::TAG_MESSAGE => VALID_CLIENT_LOGOUT_MESSAGE);
	}
	
	static function invalidLogout(){
		return array(self::TAG_CODE => INVALID_CLIENT_LOGOUT,
					self::TAG_MESSAGE => INVALID_CLIENT_LOGOUT_MESSAGE);
	}
	
	static function validQueryType(){
		return array(self::TAG_CODE => VALID_CLIENT_QUERY_TYPE,
					self::TAG_MESSAGE => VALID_CLIENT_QUERY_TYPE_MESSAGE);
	}
	
	static function invalidQueryType(){
		return array(self::TAG_CODE => INVALID_CLIENT_QUERY_TYPE,
					self::TAG_MESSAGE => INVALID_CLIENT_QUERY_TYPE_MESSAGE);
	}
	
	static function insufficientQueryParameter(){
		return array(self::TAG_CODE => INSUFFICIENT_CLIENT_QUERY_PARAMETER,
					self::TAG_MESSAGE => INSUFFICIENT_CLIENT_QUERY_PARAMETER_MESSAGE);
	}
	
	
	
	
	
}

?>