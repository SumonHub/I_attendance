<?php

namespace app\Controllers;

use app\Helpers\Utils;
use app\Controllers\Helpers\LeaveHelper;

class LeaveController
{

  private $leaveHelper;

  public function __construct()
  {
    $this->leaveHelper = new LeaveHelper();
  }

  public function addLeaveType($request, $response, $args)
  {
    $input = $request->getParams();
    $required_fields = array('name', 'balance');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $LeaveType[$field] = $input[$field];
      }
    }

    $feedback = array();
    $result = $this->leaveHelper->addLeaveType($LeaveType);
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        $tmp['balance'] = $task['balance'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function updateLeaveType($request, $response, $args)
  {
    $input = $request->getParams();
    $required_fields = array('id', 'name', 'balance');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $leaveType[$field] = $input[$field];
      }
    }

    $feedback = array();
    $result = $this->leaveHelper->updateLeaveType($leaveType);
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        $tmp['balance'] = $task['balance'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function deleteLeaveType($request, $response, $args)
  {
    $id = $args['id'];

    $feedback = array();
    $result = $this->leaveHelper->deleteLeaveType($id);
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        $tmp['balance'] = $task['balance'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function getLeaveType($request, $response, $args)
  {
    $result = $this->leaveHelper->getLeaveType();
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        $tmp['balance'] = $task['balance'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function applyLeave($request, $response, $args)
  {
    $input = $request->getParams();
    $required_fields = array('uid', 'apply_date', 'purpose', 'from_date', 'to_date', 'type_id');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $leave[$field] = $input[$field];
      }
    }

    $leave['max_type_no'] = $this->leaveHelper->getMaxTypeNo($leave['type_id'], $leave['uid']);
    
    $result = $this->leaveHelper->applyLeave($leave);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['apply_date'] = $task['apply_date'];
        $tmp['purpose'] = $task['purpose'];
        $tmp['from_date'] = $task['from_date'];
        $tmp['to_date'] = $task['to_date'];
        $tmp['type_id'] = $task['type_id'];
        $tmp['name'] = $task['name'] == null ? 'unknown' : $task['name'];
        $tmp['status'] = $task['status'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function getLeaveHistory($request, $response, $args)
  {
    $uid = $args['uid'] ?? null;
    $result = $this->leaveHelper->getLeaveHistory($uid);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['apply_date'] = $task['apply_date'];
        $tmp['purpose'] = $task['purpose'];
        $tmp['from_date'] = $task['from_date'];
        $tmp['to_date'] = $task['to_date'];
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'] == null ? 'unknown' : $task['name'];
        $tmp['status'] = $task['status'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function changeLeaveStatus($request, $response, $args)
  {
    $uid = $args['uid'];
    $input = $request->getParams();
    $required_fields = array('id', 'status');

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

    $result = $this->leaveHelper->changeLeaveStatus($uid, $param);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['apply_date'] = $task['apply_date'];
        $tmp['purpose'] = $task['purpose'];
        $tmp['from_date'] = $task['from_date'];
        $tmp['to_date'] = $task['to_date'];
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'] == null ? 'unknown' : $task['name'];
        $tmp['status'] = $task['status'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }
}
