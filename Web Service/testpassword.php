<?php

	$password = "1234";
	$cost = 10;

	// Create a random salt
	$salt = strtr(base64_encode(mcrypt_create_iv(16, MCRYPT_DEV_URANDOM)), '+', '.');
	$salt = sprintf("$2a$%02d$", $cost) . $salt;

	echo "salt => ". $salt . "<br/>";

	$hash = crypt($password, $salt);
	
	echo "hash => ". $hash . "<br/>";

	echo "===========REVERSE=============<br/>";
	
	$reverse = crypt($password, $hash);
	
	echo "back => ". $reverse . "<br/>";
	
	if ($hash === $reverse ) {
	  echo "same<br/>";
	}



?>