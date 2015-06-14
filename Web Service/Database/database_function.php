<?php

class DatabaseFunction{
	
	private $db; // using PDO 
	private $conn; // database connector
	
	// default constructor
	function __construct(){
		$dir = preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']) . "/lead";
	
		require_once "database_connect.php";
		require_once $dir. "/parser/parser.php";
		require_once $dir. "/message/message.php";
		
		$this->db = new DatabaseConnect();
		$this->conn = $this->db->connect();	
			
	}
	
	// destructor
	function __destruct(){
		
	}
	
	/**
	* This method will select username and password from User table
	* return array, consist of username and password
	**/
	public function selectLoginData($inputUsername, $inputPassword){
		// username and password in plain text
		$arr = array();
		$query = "SELECT `Lead_User_ID`, `Username`, `Password` FROM `User` WHERE `Username`=:username";
		$sql = $this->conn->prepare($query);
		$sql->bindParam(':username', $inputUsername, PDO::PARAM_INT);
		$sql->execute();
		
		if ($sql->rowCount() > 0){
			$data = $sql->fetch(PDO::FETCH_ASSOC);
			$id = $data['Lead_User_ID'];
			$dbUsername = $data['Username'];
			$dbPassword = $data['Password'];
			
			$arr = array("userID" => $id,
						"dbUsername" => $dbUsername,
						"dbPassword" => $dbPassword);
		}
		
		return $arr;
	}
	
	
	public function updateLoginDateTime($userID, $currentDate, $currentTime){
		$query = "UPDATE `User` SET `Last_login_Date`=:date, `Last_Login_Time`=:time 
					WHERE `Lead_User_ID`=:id";
					
		$sql = $this->conn->prepare($query);		
		
		$sql->bindParam(':id', $userID, PDO::PARAM_STR);
		$sql->bindParam(':date', $currentDate, PDO::PARAM_STR); //yyyy-mm-dd
		$sql->bindParam(':time', $currentTime, PDO::PARAM_STR); //hh:mm:ss
		$sql->execute();
	} 
	
	public function getTest(){
		
		/*
		$query = $this->conn->query('SELECT * FROM `database_status`');
		$query->setFetchMode(PDO::FETCH_ASSOC);
		
		echo "Number of rows ". $query->rowCount() . "<br/>";
		
		
		while($row = $query->fetch()) {
			echo $row['date'] . ", " . $row['time'] . ", " . $row['status'] ;
		}
		*/
		$query = "SELECT * FROM `database_status` WHERE `ID`=:id";
		$sql = $this->conn->prepare($query);
		
		$selectID = 2;
		
		$sql->bindParam(':id', $selectID, PDO::PARAM_INT);
		$sql->execute();
		
		if ($sql->rowCount() > 0){
			$check = $sql->fetch(PDO::FETCH_ASSOC);
			$date = $check['date'];
			// do something
		}
		
	}
	
	public function getUserJournalList($userID){
		
	}
	
	
	public function queryDatabase($sql){
		
	}

	public function insertDB(){
		$query = "INSERT INTO `database_status` (`date`, `time`, `status`) VALUES (:date, :time, :status)";
		$sql = $this->conn->prepare($query);
		
		$date = "2015-01-01";
		$time = "12:00:00";
		$status = "this is a test status";
		
		$sql->bindParam(':date', $date, PDO::PARAM_STR);
		$sql->bindParam(':time', $time, PDO::PARAM_STR);
		$sql->bindParam(':status', $status, PDO::PARAM_STR);
		
		$sql->execute();
	}
	
	public function updateDB(){
		$query = "UPDATE `database_status` SET `status`=:newStatus 
					WHERE `id`=:currentID";
		$sql = $this->conn->prepare($query);		
				
		$id = 2;
		$status = "update new status";
		
		$sql->bindParam(':currentID', $id, PDO::PARAM_INT);
		$sql->bindParam(':newStatus', $status, PDO::PARAM_STR);
		$sql->execute();	
	}
	
	public function deleteDB(){
		$query = "DELETE FROM `database_status`	WHERE `id`=:currentID";
		$sql = $this->conn->prepare($query);
		$id = 3;
		
		$sql->bindParam(':currentID', $id, PDO::PARAM_INT);
		$sql->execute();
	}

	
	
}


?>