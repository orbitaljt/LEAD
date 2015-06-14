<?php
	
	/*
	Type of query
	-------------
	100 – Login using username & password
	101 – logout
	200 – Login using facebook
	201 – logout using facebook
	600 – save journal
	700 – save experience
	*/
	
	define("TYPE_QUERY", "type");
	define("TYPE_LOGIN", "100");
	define("TYPE_LOGOUT", "101");
	define("TYPE_FACEBOOK_LOGIN", "200");
	define("TYPE_FACEBOOK_LOGOUT", "201");
	define("TYPE_CLIENT_PROFILE", "300");
	define("TYPE_JOURNAL_SAVE", "600");
	define("TYPE_EXPERIENCE_SAVE", "700");
	
	define("POST_TAG_LOGIN_USERNAME", "u");
	define("POST_TAG_LOGIN_PASSWORD", "pw");
	

	ob_start();
	date_default_timezone_set("Singapore");
	header('Content-Type: text/html; charset=utf-8');
	
	$dir = preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']) . "/lead";
	include $dir ."/message/message.php";
	include $dir ."/parser/parser.php";
	include $dir ."/logic/logic.php";

 
 	//_POST
	if(isset($_POST[TYPE_QUERY]) && !empty($_POST[TYPE_QUERY])){
		switch($_POST[TYPE_QUERY]){
			case TYPE_LOGIN:
				// if there are post parameters given
				if(isset($_POST[POST_TAG_LOGIN_USERNAME]) && isset($_POST[POST_TAG_LOGIN_PASSWORD])){
					$output = Logic::login($_POST[POST_TAG_LOGIN_USERNAME], $_POST[POST_TAG_LOGIN_PASSWORD]);
					echo $output;
				}else{
					$error = Message::insufficientQueryParameter();	
					echo Parser::arrayToJsonString($error);
				}				
				break;
			case TYPE_LOGOUT:
				break;		
			case TYPE_FACEBOOK_LOGIN:
				break;
			case TYPE_FACEBOOK_LOGOUT:
				break;
			case TYPE_JOURNAL_SAVE:
				break;
			case TYPE_EXPERIENCE_SAVE:
				break;				
		}
	}else{
		// in array
		$error = Message::invalidQueryType();
		echo Parser::arrayToJsonString($error);
	}
	
	
	
	function showError(){
		
	}
	
	



?>