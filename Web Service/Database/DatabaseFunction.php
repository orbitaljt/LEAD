<?php

class DatabaseFunction{
	
	private $db; // using PDO 
	private $conn; // database connector
	
	// default constructor
	function __construct(){
		require_once "DatabaseConnect.php";
		$this->db = new DatabaseConnect();
		$this->conn = $this->db->connect();	
			
	}
	
	// destructor
	function __destruct(){
		
	}
	
	public function getTest(){
		
		$query = $this->conn->query('SELECT * FROM `database_status`');
		$query->setFetchMode(PDO::FETCH_ASSOC);
		
		echo "Number of rows ". $query->rowCount() . "<br/>";
		
		
		while($row = $query->fetch()) {
			echo $row['date'] . ", " . $row['time'] . ", " . $row['status'] ;
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