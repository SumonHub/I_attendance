<?php

namespace app\Controllers;

use app\Helpers\Utils;
use app\Controllers\Helpers\DesignationHelper;

class DesignationController
{

  private $designationHelper;

  public function __construct()
  {
    $this->designationHelper = new DesignationHelper();
  }

  public function getDesignation($request, $response, $args)
  {
    $dpt_id = $args['dpt_id'] ?? null;
    $result = $this->designationHelper->getDesignation($dpt_id);
    $feedback = array();
    if (!is_null($result)) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['dpt_id'] = $task['dpt_id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }

    return $response->withJson(Utils::getResponse($feedback));
  }

  public function addDesignation($request, $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('dpt_id', 'name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $designation[$field] = $input[$field];
      }
    }

    $result = $this->designationHelper->addDesignation($designation);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['dpt_id'] = $task['dpt_id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function updateDesignation($request, $response, $args)
  {

    $input = $request->getParams();
    $required_fields = array('id','dpt_id', 'name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
      if (empty($input[$field])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
        return $response->withJson($feedback);
      } else {
        $designation[$field] = $input[$field];
      }
    }

    $result = $this->designationHelper->updateDesignation($designation);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['dpt_id'] = $task['dpt_id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }

  public function deleteDesignation($request, $response, $args)
  {
    $dg_id = $args['dg_id'];
    $dpt_id = $args['dpt_id'];
    $result = $this->designationHelper->deleteDesignation($dg_id, $dpt_id);
    $feedback = array();
    if ($result != null) {
      while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp['id'] = $task['id'];
        $tmp['dpt_id'] = $task['dpt_id'];
        $tmp['name'] = $task['name'];
        array_push($feedback, $tmp);
      }
    } else {
      $feedback = null;
    }
    return $response->withJson(Utils::getResponse($feedback));
  }
}
