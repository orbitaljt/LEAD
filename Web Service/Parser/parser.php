<?php

class Parser{

	static function validateUserLogin($inputUsername, $inputPassword, $dbUsername, $dbPassword){
		
		//dbPassword is hashed
		$reversePw = self::reverse($inputPassword, $dbPassword);

		if(self::isSimiliar($inputUsername, $dbUsername) && 
			self::isSimiliar($reversePw, $dbPassword)){
			return true;		
		}else{
			return false;			
		}
	}

	
	static function isSimiliar($str1, $str2){
		$val = $str1 === $str2;
		return $val;
	}

	static function arrayToJsonString($arr){
		return json_encode($arr);
	} 	
	
	/*Encryption*/
	static function getSalt(){
		// A higher "cost" is more secure but consumes more processing power
		$cost = 10;

		$salt = strtr(base64_encode(mcrypt_create_iv(16, MCRYPT_DEV_URANDOM)), '+', '.');
		// Prefix information about the hash so PHP knows how to verify it later.
		// "$2a$" Means we're using the Blowfish algorithm. The following two digits are the cost parameter.
		$salt = sprintf("$2a$%02d$", $cost) . $salt;	
	}
	
	static function encrypt($plainText, $salt){
		$hash = crypt($password, $salt);
		return $hash;
	}
	
	static function reverse($plainText, $hashValue){
		$reverse = crypt($plainText, $hashValue);
		return $reverse; //return true or false
	}
	
	
	

}


?>