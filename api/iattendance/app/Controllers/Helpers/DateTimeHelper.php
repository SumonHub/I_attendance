<?php

namespace app\Controllers\Helpers;


use app\Config\DbConnect;

class DateTimeHelper
{

    private $conn;

    public function __construct()
    {
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function getAllHoliday()
    {
        $sql = "SELECT * FROM `holiday`";

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

    public function addHoliday($holiday)
    {

        $sql = "INSERT INTO `holiday` (`name`, `date`) VALUES ('" . $holiday['name'] . "', '" . $holiday['date'] . "')";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getAllHoliday();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateHoliday($holiday)
    {

        $sql = "UPDATE `holiday` SET `name`='" . $holiday['name'] . "',`date`='" . $holiday['date'] . "' WHERE `id` = " . $holiday['id'] . "";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getAllHoliday();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteHoliday($id)
    {
        $sql = "DELETE FROM `holiday` WHERE `id` = " . $id . "";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getAllHoliday();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    //---------------------------------

    public function getOfficeTime()
    {
        $sql = "SELECT * FROM `office_time`";

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

    public function updateOfficeTime($oficeTime)
    {
        $sql = "UPDATE office_time SET starting_time= '" . $oficeTime['starting_time'] . "',ending_time= '" . $oficeTime['ending_time'] . "',status= '" . $oficeTime['status'] . "'
        WHERE id = " . $oficeTime['id'] . "";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result = $this->getOfficeTime();
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }
}
