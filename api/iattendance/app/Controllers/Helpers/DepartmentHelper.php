<?php
namespace app\Controllers\Helpers;



use app\Config\DbConnect;

class DepartmentHelper
{

    private $conn;

    public function __construct()
    {
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function addDepartment($name)
    {
        $sql = "INSERT INTO department (name) VALUES (?);";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $name);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getAllDepartment();
            }

            return null;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateDepartment($dpt)
    {
        $sql = "UPDATE department SET name = ? WHERE id =?";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->bind_param("ss", $dpt['name'], $dpt['id']);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getAllDepartment();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getAllDepartment()
    {
        $sql = "SELECT * FROM `department`";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->execute();
            $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteDepartment($id)
    {
        $sql = "DELETE FROM `department` WHERE `id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $id);
            $tasks = $stmt->execute();
            $stmt->close();
            
            if ($tasks) {
                return $this->getAllDepartment();
            }

            return null;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }
}
