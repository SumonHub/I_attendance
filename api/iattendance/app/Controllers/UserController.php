<?php

namespace app\Controllers;

use app\Helpers\Utils;
use app\Controllers\Helpers\UserHelper;

class UserController
{

  private $userHelper;

  public function __construct()
  {
    $this->userHelper = new UserHelper();
  }

  public function index($request, $response, $args)
  {
    return $response->getBody()->write("Hello from UserController");
  }

  public function login($request, $response, $args)
  {
    $_input = $request->getParams();
    $required_fields = array('uid', 'password');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($_input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $param[$field] = $_input[$field];
      }
    }

    $tmp = array();

    if ($result = $this->userHelper->getUserByCredential($param['uid'], $param['password'])) {
      $tmp['uid'] = $result['id'];
      $tmp['name'] = $result['name'];
      $tmp['phone'] = $result['phone'];
      $tmp['email'] = $result['email'];
      $tmp['dpt_id'] = $result['dpt_id'];
      $tmp['dpt_name'] = $result['dpt_name'] ?? "Unknown";
      $tmp['dg_id'] = $result['dg_id'];
      $tmp['dg_name'] = $result['dg_name'] ?? "Unknown";
      $tmp['joining_date'] = $result['joining_date'];
      $tmp['blood_group'] = $result['blood_group'];
      $tmp['role_id'] = $result['role_id'];
      $tmp['role_name'] = $result['role_name'];
      $tmp['access_code'] = $result['access_code'];
      $tmp['api_key'] = $result['api_key'];
    }

    return $response->withJson(Utils::getResponse(array($tmp)));
  }

  public function createUser($request, $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('name', 'phone', 'email', 'joining_date', 'dpt_id', 'dg_id', 'role_id', 'encoded_cv');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $user[$field] = $input[$field];
      }
    }

    //--------------------------------
    if ($this->userHelper->isExistPhone($user['phone'])) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This phone already exist.';
      return $response->withJson($feedback);
    }
    //----------------------------------

    if ($this->userHelper->isExistEmail($user['email'])) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This email already exist.';
      return $response->withJson($feedback);
    }
    //----------------------------------

    if (!empty($input['blood_group'])) {
      $user['blood_group'] = $input['blood_group'];
    }
    // todo -----------------------------
    $user['password'] = $pass = '123456';

    $encoded_cv = $user['encoded_cv'];
    unset($user['encoded_cv']);

    //-----------------------------------

    $feedback = array();
    $result = $this->userHelper->createUser($user);

    if ($result) {
      # code...
      $feedback['error'] = false;
      $feedback['msg'] = 'User added';
    } else {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'error';
    }
    return $response->withJson($feedback);
  }

  public function updateUser($request, $response, $args)
  {
    $input = $request->getParams();
    $required_fields = array('uid', 'name', 'phone', 'email', 'joining_date', 'dpt_id', 'dg_id', 'role_id');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $user[$field] = $input[$field];
      }
    }

    //--------------------------------
    if ($this->userHelper->isExistPhoneExceptCurrentUser($user['id'], $user['phone'])) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This phone already exist.';
      return $response->withJson($feedback);
    }

    //----------------------------------

    if ($this->userHelper->isExistEmailExectCurrentUser($user['id'], $user['email'])) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This email already exist.';
      return $response->withJson($feedback);
    }


    if (!empty($input['blood_group'])) {
      $user['blood_group'] = $input['blood_group'];
    } else {
      $user['blood_group'] = null;
    }

    //-----------------------------------

    $feedback = array();
    $result = $this->userHelper->updateUser($user);
    $feedback = array();
    if ($result != null) {
      while ($row = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['uid'] = $row['id'];
        $tmp['name'] = $row['name'];
        $tmp['phone'] = $row['phone'] ?? "Unknown";
        $tmp['email'] = $row['email'] ?? "Unknown";
        $tmp['dpt_id'] = $row['dpt_id'];
        $tmp['dpt_name'] = $row['dpt_name'] ?? "Unknown";
        $tmp['dg_id'] = $row['dg_id'];
        $tmp['dg_name'] = $row['dg_name'] ?? "Unknown";
        $tmp['joining_date'] = $row['joining_date'] ?? "Unknown";
        $tmp['blood_group'] = $row['blood_group'] ?? "Unknown";
        $tmp['role_id'] = $row['role_id'];
        $tmp['role_name'] = $row['role_name'];
        $tmp['access_code'] = $row['access_code'];
        array_push($feedback, $tmp);
      }
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function addUserInfo($request, $response, $args)
  {

    $uid = $args['uid'];
    $user = $request->getParams();

    if (count($user) == 0) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'No information found.';
      return $response->withJson($feedback);
    }


    if ($this->userHelper->isExistPhoneExceptCurrentUser($uid, empty($user['phone']))) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This phone already exist.';
      return $response->withJson($feedback);
    }


    if ($this->userHelper->isExistEmailExectCurrentUser($uid, empty($user['email']))) {
      $feedback['error'] = TRUE;
      $feedback['msg'] = 'This email already exist.';
      return $response->withJson($feedback);
    }

    $user['uid'] = $uid;
    $photo = null;

    if (!empty($user['p_photo'])) {
      $photo = $user['p_photo'];
      unset($user['p_photo']);
    }

    if ($result = $this->userHelper->addUserInfo($uid, $user)) {
      $feedback['error'] = false;
      $feedback['msg'] = "Successfully updated the account";
      $feedback["data"] = array();

      if ($photo != null) {
        $uploaded = $this->userHelper->uploadPhoto($uid, $photo);

        if (!$uploaded) {
          $feedback['error'] = true;
          $feedback['msg'] = "photo uploaded failed.";
          $feedback["data"] = array();
        }
      }

      return $response->withJson($feedback);
    } else {
      $feedback['error'] = true;
      $feedback['msg'] = "Cann't updated the account!";

      return $response->withJson($feedback);
    }
  }

  public function getUser($request, $response, $args)
  {

    $uid = $args['uid'] ?? null;
    $result = null;
    if ($uid != null) {
      $result = $this->userHelper->getUserById($uid);
    } else {
      $result = $this->userHelper->getAllUser();
    }

    $feedback = array();
    if ($result != null) {
      while ($row = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['uid'] = $row['id'];
        $tmp['name'] = $row['name'];
        $tmp['phone'] = $row['phone'] ?? "Unknown";
        $tmp['email'] = $row['email'] ?? "Unknown";
        $tmp['dpt_id'] = $row['dpt_id'];
        $tmp['dpt_name'] = $row['dpt_name'] ?? "Unknown";
        $tmp['dg_id'] = $row['dg_id'];
        $tmp['dg_name'] = $row['dg_name'] ?? "Unknown";
        $tmp['joining_date'] = $row['joining_date'] ?? "Unknown";
        $tmp['blood_group'] = $row['blood_group'] ?? "Unknown";
        $tmp['role_id'] = $row['role_id'];
        $tmp['role_name'] = $row['role_name'];
        $tmp['access_code'] = $row['access_code'];
        $tmp['api_key'] = $row['api_key'];
        array_push($feedback, $tmp);
      }
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function getUserInfo($request, $response, $args)
  {
    $uid = $args['uid'];
    $feedback = array();
    if ($result = $this->userHelper->getUserInfoByUid($uid)) {
      $tmp = array();
      $tmp['p_photo'] = $result['p_photo'];
      $tmp['f_name'] = $result['f_name'];
      $tmp['m_name'] = $result['m_name'];
      $tmp['dob'] = $result['dob'];
      $tmp['religion'] = $result['religion'];
      $tmp['gender'] = $result['gender'];
      $tmp['marital_status'] = $result['marital_status'];
      $tmp['nationality'] = $result['nationality'];
      $tmp['nid_no'] = $result['nid_no'];
      $tmp['c_address'] = $result['c_address'];
      $tmp['p_address'] = $result['p_address'];
      array_push($feedback, $tmp);
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function resetPassword($request, $response, $args)
  {

    $uid = $args['uid'];
    $input = $request->getParams();
    $required_fields = array('new_password');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      }
    }

    $result = $this->userHelper->updatePassword($uid, $input['new_password']);
    $feedback = array();
    if ($result != null) {
      while ($row = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['uid'] = $row['id'];
        $tmp['name'] = $row['name'];
        $tmp['phone'] = $row['phone'] ?? "Unknown";
        $tmp['email'] = $row['email'] ?? "Unknown";
        $tmp['dpt_id'] = $row['dpt_id'];
        $tmp['dpt_name'] = $row['dpt_name'] ?? "Unknown";
        $tmp['dg_id'] = $row['dg_id'];
        $tmp['dg_name'] = $row['dg_name'] ?? "Unknown";
        $tmp['joining_date'] = $row['joining_date'] ?? "Unknown";
        $tmp['blood_group'] = $row['blood_group'] ?? "Unknown";
        $tmp['role_id'] = $row['role_id'];
        $tmp['role_name'] = $row['role_name'];
        $tmp['access_code'] = $row['access_code'];
        $tmp['api_key'] = $row['api_key'];
        array_push($feedback, $tmp);
      }
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function addRole($request, $response, $args){
    $input = $request->getParams();
    $required_fields = array('role_name', 'access_code');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      }
    }

    $result = $this->userHelper->addRole($input['role_name'], $input['access_code']);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['role_name'] = $task['role_name'];
        $tmp['access_code'] = $task['access_code'];
        array_push($feedback, $tmp);
      }
    }
    return $response->withJson(Utils::getResponse($feedback));
  
  }

  public function updateRole($request, $response, $args){
    $input = $request->getParams();
    $required_fields = array('id','role_name', 'access_code');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else{
        $role[$field] = $input[$field];
      }
    }

    $result = $this->userHelper->updateRole($role);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['role_name'] = $task['role_name'];
        $tmp['access_code'] = $task['access_code'];
        array_push($feedback, $tmp);
      }
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function deleteRole($request, $response, $args){
    $id = $args['id'];
    $result = $this->userHelper->deleteRole($id);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['role_name'] = $task['role_name'];
        $tmp['access_code'] = $task['access_code'];
        array_push($feedback, $tmp);
      }
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function getRole($request, $response, $args)
  {
    $result = $this->userHelper->getRole();
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['role_name'] = $task['role_name'];
        $tmp['access_code'] = $task['access_code'];
        array_push($feedback, $tmp);
      }
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function changeUserRole($request, $response, $args)
  {
    $uid = $args['uid'];
    $input = $request->getParams();
    $required_fields = array('id','role_name', 'access_code');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      }
       else{
         $role[$field] = $input[$field];
       }
    }

    $result = $this->userHelper->changeUserRole($uid, $role);

    if ($result != null) {
      $feedback['error'] = false;
      $feedback['msg'] = "Update role successfuly.";
      $feedback['data'] = array();
    } else {
      $feedback['error'] = true;
      $feedback['msg'] = "No data found.";
    }

    return $response->withJson($feedback);
  }

  //----------------------------------------------------------------

  public function addAttendance($request, $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('uid', 'date', 'time', 'location', 'status');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $param[$field] = $input[$field];
      }
    }

    $feedback = array();
    $result = $this->userHelper->addAttendance($param);
    if ($result != null) {
      $tmp = array();
      $tmp['in_time'] = $result['in_time'];
      $tmp['in_loc'] = $result['in_loc'];
      $tmp['out_time'] = $result['out_time'];
      $tmp['out_loc'] = $result['out_loc'];
      $tmp['status'] = $result['status'];
      array_push($feedback, $tmp);
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function getAttendanceByDate($request, $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('uid', 'date');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $param[$field] = $input[$field];
      }
    }

    $result = $this->userHelper->getAttendanceByDate($param);
    $tmp = array();
    if ($result != null) {
      $tmp['in_time'] = $result['in_time'];
      $tmp['in_loc'] = $result['in_loc'];
      $tmp['out_time'] = $result['out_time'];
      $tmp['out_loc'] = $result['out_loc'];
    }
    return $response->withJson(Utils::getResponse(array($tmp)));
  }

  public function getAttendanceReportByDateRange($request, $response, $args)
  {
    $uid = $args['uid'];
    $input = $request->getParams();
    $required_fields = array('from_date', 'to_date');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $param[$field] = $input[$field];
      }
    }

    $result = $this->userHelper->getAttendanceReportByDateRange($uid, $param['from_date'], $param['to_date']);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['uid'] = $task['uid'];
        $tmp['date'] = $task['date'];
        $tmp['in_time'] = $task['in_time'];
        $tmp['in_loc'] = $task['in_loc'];
        $tmp['out_time'] = $task['out_time'];
        $tmp['out_loc'] = $task['out_loc'];
        $tmp['status'] = $task['status'];
        $tmp['late_status'] = $task['late_status'];
        array_push($feedback, $tmp);
      }
    }
    return $response->withJson(Utils::getResponse($feedback));
  }
}
