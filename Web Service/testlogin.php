<?php
	date_default_timezone_set("Singapore");
	header('Content-Type: text/html; charset=utf-8');
	
	//echo dirname(__FILE__);
	//echo preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']);
	
	require_once('logic/logic.php');

	$output = Logic::login('testuser', '1234');
	echo $output;
	echo "<br/>";
	echo date("Y-m-d");
	echo "<br/>";
	echo date("H:i:s");
?>