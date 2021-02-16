<?php
namespace app\Helpers;

use app\Config\DbConnect;


class DbHelper
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

    //-------------create user ------------------

    public function createUser($param)
    {

        $keys = implode(', ', array_keys($param));
        $values = "'" . implode("','", array_values($param)) . "'";
        $sql = "INSERT INTO user ($keys) VALUES ($values)";

        // var_dump($sql);
        // exit;

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

    public function uploadCv($api_key, $encoded_cv){

        $filename = time().'.pdf';
        $path = dirname(__FILE__) . '/../uploads';
        $filepath = dirname(__FILE__) . '/../uploads/' . $filename;
        
        // echo $filepath;
        // exit;

        if (!file_exists($path)) {
            mkdir($path, 0777, true);
        }
        
        $sql = "UPDATE user SET cv_url = '$filename' WHERE api_key = '$api_key'";
        
         if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();

            if(!$result){
                return false;
            }

            file_put_contents($filepath, base64_decode($encoded_cv));

            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }

        if(mysqli_query($con,$sql)){
        file_put_contents($path,base64_decode($encoded_cv));
        echo "Successfully Uploaded";
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
        // var_dump($param);
        // exit;

        if($this->isExistUserInfo($uid)){

            $keys = array_keys($param);
            $values = array_values($param);
            $query = '';
            foreach ( $param as $key => $value ){
                $and = '';
                if($query != ''){
                    $and = ' , ';
                }
                $query = $query. $and. " `". $key. "` = '".$value."' ";
            }
        
            $sql = "UPDATE `user_info` SET $query WHERE `uid` = $uid;";

        } else{
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


    public function uploadPhoto($uid, $encoded_photo){
       
        $filename = time().'.png';
        $path = dirname(__FILE__) . '/../uploads';
        $filepath = dirname(__FILE__) . '/../uploads/' . $filename;

        if (!file_exists($path)) {
            mkdir($path, 0777, true);
        }

        $sql = "UPDATE user_info SET p_photo = '$filename' WHERE uid = '$uid'";

        
        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();

            if(!$result){
                return false;
            }

            file_put_contents($filepath, base64_decode($encoded_photo));

            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }

        if(mysqli_query($con,$sql)){
        file_put_contents($path,base64_decode($encoded_cv));
        echo "Successfully Uploaded";
        }
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

    //----------------- updateUser ------------------

    public function updateUser($param)
    {
        $sql = "UPDATE user SET name= '" . $param['name'] . "',phone='" . $param['phone'] . "',
        email='" . $param['email'] . "',dg_id= " . $param['dg_id'] . ",dpt_id=" . $param['dpt_id'] . ",
        joining_date='" . $param['joining_date'] . "',blood_group='" . $param['blood_group'] . "', role_id='" . $param['role_id'] . "'
        WHERE api_key = '" . $param['api_key'] . "'";

        // var_dump($sql);
        // exit;

        if ($stmt = $this->conn->prepare($sql)) {
            // $stmt->bind_param("sssssssss", $param['name'], $param['phone'], $param['email'],
            // $param['dg_id'], $param['dpt_id'], $param['joining_date'], $param['blood_group'],
            // $param['role_id'],
            // $param['api_key']);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }


    //---------------- login--------------
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

    //--------------- reset password -----------------

    public function updatePassword($api_key, $new_password)
    {
        $sql = "UPDATE user SET password = ? WHERE api_key = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $new_password, $api_key);
            $stmt->execute();
            $stmt->close();
            return true;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }


    // ------------------ get user by api_key ---------------

    public function getUserByKey($key)
    {
        $sql = "SELECT user.*, user_role.role_name , user_role.access_code, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN user_role ON user.role_id = user_role.id
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id
        WHERE user.api_key= ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $key);
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

    // --------- get user basic information by api_key --------

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

    //------------- All User ---------------

    public function getAllUser($api_key)
    {
        $sql = "SELECT user.*, user_role.role_name , user_role.access_code, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN user_role ON user.role_id = user_role.id
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id
        WHERE `api_key` != ? and user_role.access_code != 111111
        ORDER BY id ASC";


        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $api_key);
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

    public function createUserRole($role_name, $access_code)
    {
        $sql = "INSERT INTO `user_role`(`role_name`, `access_code`) VALUES ('$role_name',$access_code)";

        if ($tasks = $this->conn->query($sql)) {
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateUserRole($id, $role_name, $access_code)
    {
        $sql = "UPDATE `user_role` SET `role_name`= ?, `access_code`= ?  WHERE `id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("sss", $role_name, $access_code, $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getUserrole()
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

    //----------------- leave type ------------------

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

    public function createLeaveType($name, $balance)
    {
        $sql = "INSERT INTO leave_type (name, balance) VALUES (?, ?);";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $name, $balance);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateLeaveType($type_id, $name, $balance)
    {
        $sql = "UPDATE `leave_type` SET `name`= '$name',  `balance`= '$balance' WHERE `type_id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $type_id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function actionLeaveType($param)
    {
        // INSERT INTO leave_type (name, balance) VALUES (?, ?);


        // add
        if ($param['action_code'] == 1) {
            $sql = "INSERT INTO `leave_type` (`name`, `balance`) VALUES ('" . $param['name'] . "', '" . $param['balance'] . "')";
        }

        // update
        if ($param['action_code'] == 2) {
            $sql = "UPDATE `leave_type` SET `name`='" . $param['name'] . "',`balance`='" . $param['balance'] . "' WHERE `type_id` = " . $param['type_id'] . "";
        }

        // delete
        if ($param['action_code'] == 3) {
            $sql = "DELETE FROM `leave_type` WHERE `type_id` = " . $param['type_id'] . "";
        }

        if ($stmt = $this->conn->prepare($sql)) {
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function getMaxTypeNo($type_id, $uid)
    {
        $sql = "SELECT COUNT(id) as max_type_no FROM leave_history WHERE type_id = ? and uid = ? and status = 1";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $type_id, $uid);
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

    public function applyLeave($param)
    {
        $keys = implode(', ', array_keys($param));
        $values = "'" . implode("','", array_values($param)) . "'";
        $sql = "INSERT INTO leave_history ($keys) VALUES ($values)";

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

    public function getLeaveHistory($uid)
    {
        $sql = "SELECT leave_history.* , leave_type.name 
        FROM `leave_history` 
        LEFT JOIN leave_type 
        ON leave_history.type_id = leave_type.type_id 
        WHERE leave_history.uid = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $uid);
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

    public function getAllLeaveHistory()
    {
        $sql = "SELECT leave_history.id, leave_history.apply_date, leave_history.purpose, leave_history.from_date, leave_history.to_date, leave_history.status, leave_type.type_id, leave_type.name, userinfo.uname, userinfo.dpt_name , userinfo.dg_name
        FROM `leave_history` 
        LEFT JOIN (SELECT user.id as uid, user.name as uname, department.name as dpt_name, designation.name as dg_name
        FROM user 
        LEFT JOIN department ON user.dpt_id = department.id
        LEFT JOIN designation ON user.dg_id = designation.id) AS userinfo ON leave_history.uid = userinfo.uid
        LEFT JOIN leave_type ON leave_history.type_id = leave_type.type_id 
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

    public function changeLeaveStatus($lid, $status)
    {
        $sql = "UPDATE leave_history SET status = '" . $status . "' WHERE id ='" . $lid . "'";

        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();
            // var_dump();
            // exit;
            //$stmt->bind_param("ss", $status, $lid);
            // $stmt->execute();
            // $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    // ---------------- Department ------------------

    public function createDepartment($name)
    {
        $sql = "INSERT INTO department (name) VALUES (?);";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $name);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateDepartment($id, $name)
    {
        $sql = "UPDATE department SET name = ? WHERE id =?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $name, $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
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
        $deleteAllDgOfDpt = $this->deleteAllDgOfDpt($id);
        if (!$deleteAllDgOfDpt) {
            return null;
        }

        $sql = "DELETE FROM `department` WHERE `id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteAllDgOfDpt($id)
    {
        $sql = "DELETE FROM `designation` WHERE `dpt_id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    //----------------- Designation -----------------

    public function getDesignation($dpt_id)
    {
        $sql = "SELECT * FROM `designation` where dpt_id = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $dpt_id);
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

    public function createDesignation($dpt_id, $name)
    {
        $sql = "INSERT INTO designation(dpt_id, name) VALUES (?,?)";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $dpt_id, $name);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function updateDesignation($id, $name)
    {
        $sql = "UPDATE designation SET name = ? WHERE id =?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("ss", $name, $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
            $error = "\n\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function deleteDesignation($id)
    {
        $sql = "DELETE FROM `designation` WHERE `id` = ?";

        if ($stmt = $this->conn->prepare($sql)) {
            $stmt->bind_param("s", $id);
            $tasks = $stmt->execute();
            $stmt->close();
            return $tasks;
        } else {
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
            }
        } elseif ($param['status'] == 2) {
            // update out time
            $sql = "UPDATE `attendance` SET `out_time`= '" . $param['time'] . "', `out_loc`= '" . $param['location'] . "', `status`= '2' 
            WHERE uid = '" . $param['uid'] . "' and date = '" . $param['date'] . "'";
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
         WHEN attendance.in_time>office_time.starting_time AND attendance.out_time<office_time.ending_time 
            THEN 2
         WHEN attendance.in_time>office_time.starting_time THEN 3
         WHEN attendance.in_time<office_time.starting_time THEN 4
         ELSE 0
     END AS late_status
    from attendance
    INNER JOIN office_time
    ON IF(WEEKDAY(attendance.date)+2>7, 7-WEEKDAY(attendance.date), WEEKDAY(attendance.date)+2) = office_time.id
    where date(attendance.date) between date('$from_date') and date('$to_date') and attendance.uid = $uid
    order by date ASC";

        if ($stmt = $this->conn->prepare($sql)) {
            //  $stmt->bind_param("sss", $from_date, $to_date, $uid);
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

    //----------------Holiday----------------------

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

    public function addOrUpdateOrDeleteHoliday($param)
    {

        // add
        if ($param['status'] == 1) {
            $sql = "INSERT INTO `holiday` (`name`, `date`) VALUES ('" . $param['name'] . "', '" . $param['date'] . "')";
        }

        // update
        if ($param['status'] == 2) {
            $sql = "UPDATE `holiday` SET `name`='" . $param['name'] . "',`date`='" . $param['date'] . "' WHERE `id` = " . $param['id'] . "";
        }

        // delete
        if ($param['status'] == 3) {
            $sql = "DELETE FROM `holiday` WHERE `id` = " . $param['id'] . "";
        }


        if ($stmt = $this->conn->prepare($sql)) {
            $result = $stmt->execute();

            if ($result) {
                $result = $this->getAllHoliday();
            } else {
                $result = null;
            }

            return $result;
        } else {
            $error = "\n" . $this->conn->errno . ' ' . $this->conn->error;
            echo $error;
            return null;
        }
    }

    public function checkOffDayByKey($uid){

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

    public function updateOfficeTime($param)
    {
        $sql = "UPDATE `office_time` SET `starting_time`= '" . $param['starting_time'] . "',`ending_time`= '" . $param['ending_time'] . "',`status`= '" . $param['status'] . "'
        WHERE id = " . $param['id'] . "";

        // var_dump($sql);
        // exit;

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

    public function checkOffdayInHoliday() {

        $date = date('Y-m-d');
        
        $sql = "SELECT * FROM `holiday` WHERE `date` = '$date'";

        // var_dump($sql);
        // exit;

        if ($stmt = $this->conn->prepare($sql)) {
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

    public function checkOffdayInOfficeTime() {

        $date = date('Y-m-d');
        
        $sql = "SELECT * FROM `office_time` 
        WHERE IF(WEEKDAY('$date')+2>7, 7-WEEKDAY('$date'), WEEKDAY('$date')+2) = office_time.id
        AND office_time.status = '2'";

        // var_dump($sql);
        // exit;

        if ($stmt = $this->conn->prepare($sql)) {
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

    public function checkIsUserGetLeave() {
        $date = date('Y-m-d');
        
        $sql = "SELECT * FROM leave_history 
        WHERE date('$date') between date(`from_date`) and date(`to_date`) 
        AND uid = 1 
        AND status = '1'";

        // var_dump($sql);
        // exit;

        if ($stmt = $this->conn->prepare($sql)) {
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
}
