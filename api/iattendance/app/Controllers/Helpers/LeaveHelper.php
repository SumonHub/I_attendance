<?php

namespace app\Controllers\Helpers;

use app\Config\DbConnect;


class LeaveHelper
{
    private $conn;

    public function __construct()
    {
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function addLeaveType($leaveType)
    {
        $sql = "INSERT INTO `leave_type` (`name`, `balance`) VALUES ('" . $leaveType['name'] . "', '" . $leaveType['balance'] . "')";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();
            if ($tasks) {
                $stmt = $this->getLeaveType();
            }
            return $stmt;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateLeaveType($leaveType)
    {
        $sql = "UPDATE `leave_type` SET `name`='" . $leaveType['name'] . "',`balance`='" . $leaveType['balance'] . "' WHERE `id` = " . $leaveType['id'] . "";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getLeaveType();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteLeaveType($id)
    {
        $sql = "DELETE FROM `leave_type` WHERE `id` = " . $id . "";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getLeaveType();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getLeaveType()
    {
        $sql = "SELECT * FROM leave_type";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->execute();
            $tasks = $stmt->get_result();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getMaxTypeNo($id, $uid)
    {
        $sql = "SELECT COUNT(id) as max_type_no FROM leave_history WHERE type_id = ? and uid = ? and status = 1";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $id, $uid);
            $stmt->execute();
            $task = $stmt->get_result()->fetch_assoc();
            $max_type_no = $task['max_type_no'] + 1;
            $stmt->close();
            return $max_type_no;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function applyLeave($leave)
    {
        $keys = implode(', ', array_keys($leave));
        $values = "'" . implode("','", array_values($leave)) . "'";
        $sql = "INSERT INTO leave_history ($keys) VALUES ($values)";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();
            if ($tasks) {
                $stmt = $this->getLeaveHistory($leave['uid']);
            }
            return $stmt;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getLeaveHistory($uid)
    {
        $whreClouse = '';
        if ($uid != null) {
            $whreClouse = 'WHERE leave_history.uid = ' . $uid;
        }

        $sql = "SELECT leave_history.* , leave_type.name 
        FROM `leave_history` 
        LEFT JOIN leave_type 
        ON leave_history.id = leave_type.id " . $whreClouse;

        try {
            $stmt = $this->conn->prepare($sql);
            $stmt->execute();
            $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getAllLeaveHistory()
    {
        $sql = "SELECT leave_history.id, leave_history.apply_date, leave_history.purpose, leave_history.from_date, leave_history.to_date, leave_history.status, leave_type.id, leave_type.name, userinfo.uname, userinfo.dpt_name , userinfo.dg_name
        FROM `leave_history` 
        LEFT JOIN (SELECT user.id as uid, user.name as uname, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id) AS userinfo ON leave_history.uid = userinfo.uid
        LEFT JOIN leave_type ON leave_history.id = leave_type.id 
        ORDER BY `created_at` DESC";

        if ($stmt = $this->conn->prepare($sql)) {
            //$stmt->bind_param("s", $uid);
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

    public function changeLeaveStatus($uid, $leave)
    {
        $sql = "UPDATE leave_history SET status = '" . $leave['status'] . "' WHERE uid ='" . $uid . "'";

        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                # code...
                $result = $this->getLeaveHistory($uid);
            }
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }
}
