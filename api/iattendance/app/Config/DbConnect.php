<?php
namespace app\Config;

use mysqli;
use app\Config\Config;
 
class DbConnect {
 
    private $conn;
 
    function __construct() {        
    }
 
    function connect() {
        
        $this->conn = new mysqli(
            Config::DB_HOST, Config::DB_USERNAME, Config::DB_PASSWORD, Config::DB_NAME);
        if ($this->conn->connect_error) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        }
        return $this->conn;
    }
 
}
 
?>