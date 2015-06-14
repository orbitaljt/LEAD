<?php


class DatabaseConnect{
	
	private $conn;
	
	function __construct(){ //default constructor
		
	}

	function __destruct(){
		$conn = null;
	}

		
	public function connect(){
		require_once 'configuration.php';
		//$conn = mysqli_connect(self::$dbHost, self::$dbUsername, self::$dbPassword, self::$dbName) or die(mysqli_connect_error());

		try{
			$conn = new PDO("mysql:host=". DB_HOST .";dbname=". DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			$conn->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
 
			return $conn;
			
		}catch(PDOException $e){
			echo $e->getMessage();
			return null;
		}

	}//end connect
	
	
	
}//end Database

?>

