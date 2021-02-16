<?php

namespace app\Controllers;

use app\Helpers\Utils;
use app\Helpers\DbHelper;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class HomeController
{
  public function index(Request $request, Response $response, array $args)
  {
    return $response->getBody()->write("Hello from HomeController");
  }

  public function login(Request $request, Response $response, array $args)
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

    $db = new DbHelper();

    $tmp = array();

    if ($result = $db->getUserByCredential($param['uid'], $param['password'])) {
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

  public function createUser(Request $request, Response $response, array $args){

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";
        return $response->withJson($feedback);
    }

    $input = $request->getParams();
    $required_fields = array('name', 'phone', 'email', 'joining_date', 'dpt_id', 'dg_id', 'role_id', 'encoded_cv');

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

    //--------------------------------
    if ($db->isExistPhone($param['phone'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This phone already exist.';
        return $response->withJson($feedback);
    }
    //----------------------------------

    if ($db->isExistEmail($param['email'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This email already exist.';
        return $response->withJson($feedback);
    }
    //----------------------------------

    if (!empty($input['blood_group'])) {
        $param['blood_group'] = $input['blood_group'];
    }
    // todo -----------------------------
    $param['password'] = $pass = '123456';
    $param['api_key'] = $api_key = md5(time());

    $encoded_cv = $param['encoded_cv'];
    unset($param['encoded_cv']);

    //-----------------------------------

    $feedback = array();
    
    if ($db->createUser($param)) {
        $uploaded = $db->uploadCv($api_key, $encoded_cv);
    }
    
    return $response->withJson($feedback);
    
  }
}
