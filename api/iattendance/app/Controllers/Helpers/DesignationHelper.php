<?php

namespace app\Controllers\Helpers;

use app\Config\DbConnect;

class DesignationHelper
{
    private $conn;

    public function __construct()
    {
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function getDesignation($dpt_id)
    {

        $whereClause = '';
        if ($dpt_id != null) {
            # code...
            $whereClause = 'WHERE dpt_id = ' . $dpt_id;
        }

        $sql = "SELECT * FROM designation " . $whereClause;

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->execute();
            $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function addDesignation($designation)
    {
        $sql = "INSERT INTO designation(dpt_id, name) VALUES (?,?)";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->bind_param("ss", $designation['dpt_id'], $designation['name']);
            $tasks = $stmt->execute();
            if ($tasks) {
                # code...
                $tasks = $this->getDesignation($designation['dpt_id']);
            }
            $stmt->close();
            return $tasks;
        } catch (\Throwable $th) {
            //throw $th;

            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateDesignation($designation)
    {
        $sql = "UPDATE designation SET name = ? WHERE id =?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $designation['name'], $designation['id']);
            $tasks = $stmt->execute();
            if ($tasks) {
                $tasks = $this->getDesignation($designation['dpt_id']);
            }
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteDesignation($dg_id, $dpt_id)
    {
        $sql = "DELETE FROM designation WHERE id = ? AND dpt_id = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $dg_id, $dpt_id);
            $tasks = $stmt->execute();
            if ($tasks) {
                $tasks =  $this->getDesignation($dpt_id);
            }
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }
}
