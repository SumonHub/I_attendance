<?php

namespace app\Controllers\Helpers;

use app\Config\DbConnect;


class UserHelper
{
    private $conn;

    public function __construct()
    {
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function isValidApiKey($api_key)
    {
        $sql = "SELECT id from user WHERE api_key= ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $api_key);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return false;
        }
    }

    public function createUser($param)
    {

        $keys = implode(', ', array_keys($param));
        $values = "'" . implode("','", array_values($param)) . "'";
        $sql = "INSERT INTO user ($keys) VALUES ($values)";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function uploadCv($uid, $encoded_cv)
    {

        $filename = time() . '.pdf';
        $path = dirname(__FILE__) . '/../../../public/assets/cvs/';
        $filepath = $path . $filename;

        if (!file_exists($path)) {
            mkdir($path, 0777, true);
        }

        $sql = "UPDATE user SET cv_url = '$filename' WHERE id = '$uid'";

        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();
            if ($result) {
                file_put_contents($filepath, base64_decode($encoded_cv));
            }
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function isExistUserInfo($uid)
    {
        $sql = "SELECT * FROM `user_info` where uid = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function addUserInfo($uid, $param)
    {
        if ($this->isExistUserInfo($uid)) {
            $keys = array_keys($param);
            $values = array_values($param);
            $query = '';
            foreach ($param as $key => $value) {
                $and = '';
                if ($query != '') {
                    $and = ' , ';
                }
                $query = $query . $and . " `" . $key . "` = '" . $value . "' ";
            }

            $sql = "UPDATE `user_info` SET $query WHERE `uid` = $uid;";
        } else {
            $keys = implode(', ', array_keys($param));
            $values = "'" . implode("','", array_values($param)) . "'";
            $sql = "INSERT INTO user_info ($keys) VALUES ($values)";
        }


        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function uploadPhoto($uid, $encoded_photo)
    {

        $filename = time() . '.png';
        $path = dirname(__FILE__) . '/../../../public/assets/cvs/';
        $filepath = $path . $filename;

        if (!file_exists($path)) {
            mkdir($path, 0777, true);
        }

        $sql = "UPDATE user_info SET p_photo = '$filename' WHERE uid = '$uid'";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $result = $stmt->execute();
            if ($result) {
                file_put_contents($filepath, base64_decode($encoded_photo));
            }
            $stmt->close();
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }

        // if (mysqli_query($this->con, $sql)) {
        //     file_put_contents($path, base64_decode($encoded_cv));
        //     echo "Successfully Uploaded";
        // }
    }

    public function isExistPhone($phone)
    {
        $sql = "SELECT * FROM `user` where phone = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $phone);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function isExistPhoneExceptCurrentUser($api_key, $phone)
    {
        $sql = "SELECT * FROM `user` WHERE `phone` = ? AND `api_key` != ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $phone, $api_key);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function isExistEmail($email)
    {
        $sql = "SELECT * FROM user WHERE email = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function isExistEmailExectCurrentUser($api_key, $email)
    {
        $sql = "SELECT * FROM `user` WHERE `email` = ? AND `api_key` != ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $email, $api_key);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateUser($param)
    {
        $sql = "UPDATE user SET name= '" . $param['name'] . "',phone='" . $param['phone'] . "',
        email='" . $param['email'] . "',dg_id= " . $param['dg_id'] . ",dpt_id=" . $param['dpt_id'] . ",
        joining_date='" . $param['joining_date'] . "',blood_group='" . $param['blood_group'] . "', role_id='" . $param['role_id'] . "'
        WHERE id = '" . $param['id'] . "'";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result =  $this->getUserById($param['id']);
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getUserByCredential($id, $pass)
    {
        //  $password = md5($pass);

        $sql = 'SELECT user.*, user_role.role_name , user_role.access_code, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN user_role ON user.role_id = user_role.id
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id
        WHERE user.id= ? or user.name = ? AND user.password = ?';

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("sss", $id, $id, $pass);
            $stmt->execute();
            $task = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $task;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updatePassword($uid, $new_password)
    {
        $sql = "UPDATE user SET password = ? WHERE uid = ?";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result =  $this->getUserById($uid);
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getUserById($uid)
    {
        $sql = "SELECT user.*, user_role.role_name , user_role.access_code, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN user_role ON user.role_id = user_role.id
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id
        WHERE user.id= ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $uid);
            $stmt->execute();
            $task = $stmt->get_result();
            $stmt->close();
            return $task;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getAllUser()
    {
        $sql = "SELECT user.*, user_role.role_name , user_role.access_code, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN user_role ON user.role_id = user_role.id
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id
        WHERE user_role.access_code != 111111
        ORDER BY id ASC";


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

    public function getUserInfoByUid($uid)
    {
        $sql = "SELECT * from  user_info WHERE uid = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $task = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $task;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    //--------------- Create role -------------

    public function isExistAccesscode($access_code)
    {
        $sql = "SELECT * FROM `user_role` where access_code = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $access_code);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getRole()
    {
        $sql = "SELECT * FROM `user_role` ORDER BY created_at ASC";

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

    public function addRole($role_name, $access_code)
    {
        $sql = "INSERT INTO `user_role`(`role_name`, `access_code`) VALUES ('$role_name',$access_code)";

        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $tasks = $stmt->execute();
            $stmt->close();

            if ($tasks) {
                return $this->getRole();
            }

            return null;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateRole($role)
    {
        $sql = "UPDATE `user_role` SET `role_name`= ?, `access_code`= ?  WHERE `id` = ?";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->bind_param("sss", $role['role_name'], $role['access_code'], $role['id']);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result =  $this->getUserById($role['id']);
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteRole($id)
    {
        $sql = "UPDATE `user_role` SET `role_name`= ?, `access_code`= ?  WHERE `id` = ?";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->bind_param("s", $id);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result =  $this->getUserById($id);
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function changeUserRole($uid, $role)
    {
        $sql = "UPDATE `user` SET `role_id`= ? WHERE `id` = ?";
        try {
            //code...
            $stmt = $this->conn->prepare($sql);
            $stmt->bind_param("sss", $role['role_id'], $uid);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                $result =  $this->getUserById($role['id']);
            }
            return $result;
        } catch (\Throwable $th) {
            //throw $th;
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    //----------------Attendance----------------------

    public function addAttendance($param)
    {

        if ($param['status'] == 1) {
            // insert in time
            $todayInsert = $this->getAttendanceByDate($param);
            if ($todayInsert == null) {
                // insert in
                $sql = "INSERT INTO `attendance` (`uid`, `date`, `in_time`, `in_loc`, `status`) 
                VALUES ('" . $param['uid'] . "', '" . $param['date'] . "', '" . $param['time'] . "', '" . $param['location'] . "', '1')";
            } else {
                return $todayInsert;
            }
        } elseif ($param['status'] == 2) {
            // update out time
            $sql = "UPDATE `attendance` SET `out_time`= '" . $param['time'] . "', `out_loc`= '" . $param['location'] . "', `status`= '2' 
            WHERE uid = '" . $param['uid'] . "' and date = '" . $param['date'] . "'";
        }


        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();
            if ($result) {
                $result = $this->getAttendanceByDate($param);
            }
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getAttendanceByDate($param)
    {
        $sql = "SELECT * FROM `attendance` WHERE uid = ? AND date = ?";
        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $param['uid'], $param['date']);
            $stmt->execute();
            $result = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getAttendanceReportByDateRange($uid, $from_date, $to_date)
    {
        $sql = "SELECT attendance.*,
        CASE
            WHEN attendance.out_time IS NULL THEN 1
            WHEN attendance.in_time>office_time.starting_time 
                AND attendance.out_time<office_time.ending_time THEN 2
            WHEN attendance.in_time>office_time.starting_time THEN 3
            WHEN attendance.in_time<office_time.starting_time THEN 4
            ELSE 0
        END AS late_status
        from attendance
        INNER JOIN office_time
        ON IF(WEEKDAY(attendance.date)+2>7, 7-WEEKDAY(attendance.date), WEEKDAY(attendance.date)+2) = office_time.id
        where date(attendance.date) between date('$from_date') and date('$to_date') 
        AND attendance.uid = $uid
        order by date ASC";

        try {
            //code...
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
}
