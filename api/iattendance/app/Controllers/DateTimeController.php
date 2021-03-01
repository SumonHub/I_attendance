<?php
namespace app\Controllers;

use app\Helpers\Utils;
use app\Controllers\Helpers\DateTimeHelper;

class DateTimeController{

  private $dateTimeHelper;

  public function __construct()
  {
    $this->dateTimeHelper = new DateTimeHelper();
  }
  
    public function getAllHoliday( $request,  $response,  $args)
    {
      $result = $this->dateTimeHelper->getAllHoliday();
      $feedback = array();
      if ($result) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['name'] = $task['name'];
          $tmp['date'] = $task['date'];
          array_push($feedback, $tmp);
        }
      }
      return $response->withJson(Utils::getResponse($feedback));
    }
    
    public function addHoliday( $request,  $response,  $args)
    {
      $input = $request->getParams();
      $required_fields = array('name', 'date');
      
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
  
      $result = $this->dateTimeHelper->addHoliday($param);
      $feedback = array();
      if ($result != null) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['name'] = $task['name'];
          $tmp['date'] = $task['date'];
          array_push($feedback, $tmp);
        }
      } else{
        $feedback = null;
      }
  
      return $response->withJson(Utils::getResponse($feedback));
    }
    
    public function updateHoliday($request, $response, $args)
    {
      $input = $request->getParams();
      $required_fields = array('id', 'name', 'date');
      
      // Handling request params null exception
      foreach ($required_fields as $field) {
        if (empty($input[$field])) {
          $feedback['error'] = TRUE;
          $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
          return $response->withJson($feedback);
        } else {
          $holiday[$field] = $input[$field];
        }
      }
  
      $result = $this->dateTimeHelper->updateHoliday($holiday);
      $feedback = array();
      if ($result != null) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['name'] = $task['name'];
          $tmp['date'] = $task['date'];
          array_push($feedback, $tmp);
        }
      }else{
        $feedback = null;
      }
  
      return $response->withJson(Utils::getResponse($feedback));
    }
    
    public function deleteHoliday($request, $response, $args)
    {
      $id = $args['id'];
  
      $result = $this->dateTimeHelper->deleteHoliday($id);
      $feedback = array();
      if ($result != null) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['name'] = $task['name'];
          $tmp['date'] = $task['date'];
          array_push($feedback, $tmp);
        }
      }else{
        $feedback = null;
      }
  
      return $response->withJson(Utils::getResponse($feedback));
    }
  
    public function getOfficeTime($request, $response, $args)
    {
  
      $result = $this->dateTimeHelper->getOfficeTime();
      $feedback = array();
      if ($result) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['day_name'] = $task['day_name'];
          $tmp['starting_time'] = $task['starting_time'];
          $tmp['ending_time'] = $task['ending_time'];
          $tmp['status'] = $task['status'];
          array_push($feedback, $tmp);
        }
      }else{
        $feedback = null;
      }
      return $response->withJson(Utils::getResponse($feedback));
    }
  
    public function updateOfficeTime($request, $response, $args)
    {

      $input = $request->getParams();
      $required_fields = array('id', 'starting_time', 'ending_time', 'status');
      
      // Handling request params null exception
      foreach ($required_fields as $field) {
        if (empty($input[$field])) {
          $feedback['error'] = TRUE;
          $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
          return $response->withJson($feedback);
        } else {
          $oficeTime[$field] = $input[$field];
        }
      }

      $result = $this->dateTimeHelper->updateOfficeTime($oficeTime);
      $feedback = array();
      if ($result) {
        while ($task = $result->fetch_assoc()) {
          $tmp = array();
          $tmp['id'] = $task['id'];
          $tmp['day_name'] = $task['day_name'];
          $tmp['starting_time'] = $task['starting_time'];
          $tmp['ending_time'] = $task['ending_time'];
          $tmp['status'] = $task['status'];
          array_push($feedback, $tmp);
        }
      }else{
        $feedback = null;
      }
      return $response->withJson(Utils::getResponse($feedback));
    }
}