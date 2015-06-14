<?php
//echo dirname(getcwd());
//echo preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']);
	$dir = preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']) . "/lead";
	
	require_once $dir ."/parser/parser.php";
	require_once $dir ."/message/message.php";
	require_once $dir ."/database/database_function.php";

class Logic{
	
	/**
	* This method will access the login method.
	* Firstly, it will get user information from database.
	* Secondly, it will then validate username and password if there's data in the database.
	* 
	* @$username	-	string of username from user input
	* @$password	-	string of password from user input
	*
	* Return message array - valid or invalid
	**/
	static function login($username, $password){
		$arrayMsg = array();
		$df = new DatabaseFunction();
		// result in array
		$result = $df->selectLoginData($username, $password);
		if(count($result) > 0){
			// data available
			$userID = $result["userID"];
			$dbUsername = $result["dbUsername"];
			$dbPassword = $result["dbPassword"];
			
			// output either true or false
			$output = Parser::validateUserLogin($username, $password, $dbUsername, $dbPassword);
			if($output){ // true
				$arrayMsg = Message::validLogin();	
				
				$currentDate = date("Y-m-d");
				$currentTime = date("H:i:s");;
				
				// update login date & time
				$df->updateLoginDateTime($userID, $currentDate, $currentTime);
						
			}else{ // false
				$arrayMsg = Message::invalidLogin();
			}
			
			// return success message	
		}else{
			$arrayMsg = Message::invalidLogin();
			// return error message
		}
		
		return Parser::arrayToJsonString($arrayMsg);
	}
	
	
	
	
}

?>