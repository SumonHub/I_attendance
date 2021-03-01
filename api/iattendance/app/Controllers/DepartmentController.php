<?php

namespace app\Controllers;

use app\Helpers\Utils;
use app\Controllers\Helpers\DepartmentHelper;

class DepartmentController
{

  private $departmentHelper;

  public function __construct()
  {
    $this->departmentHelper = new DepartmentHelper();
  }

  public function getAllDepartment($request, $response, $args)
  {
    $result = $this->departmentHelper->getAllDepartment();
    $feedback = array();
    if ($result) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function addDepartment($request,  $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('name');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $dpt_name = $input[$field];
      }
    }

    $result = $this->departmentHelper->addDepartment($dpt_name);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function updateDepartment($request,  $response, $args)
  {
    $input = $request->getParams();
    $required_fields = array('id', 'name');
    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $dpt[$field] = $input[$field];
      }
    }

    $result = $this->departmentHelper->updateDepartment($dpt);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function deleteDepartment($request,  $response, $args)
  {
    $dpt_id = $args['dpt_id'];
    $result = $this->departmentHelper->deleteDepartment($dpt_id);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }
}
