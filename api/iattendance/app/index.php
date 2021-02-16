<?php

use Slim\App;
use app\Helpers\DbHelper;
use app\Controllers\HomeController;
use \Psr\Http\Message\ResponseInterface as Response;
use \Psr\Http\Message\ServerRequestInterface as Request;

require '../vendor/autoload.php';

$app = new App([
    'settings' => [
        'displayErrorDetails' => true
    ]
]);

$app->get('/', HomeController::class . ':index');

//---------------------User--------------------

$app->post('/login', HomeController::class . ':login');
$app->post('/createUser', HomeController::class . ':createUser');


$app->post('/updateUser', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    $input = $request->getParams();
    $required_fields = array('name', 'phone', 'email', 'joining_date', 'dpt_id', 'dg_id', 'role_id');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    //--------------------------------
    if ($db->isExistPhoneExceptCurrentUser($header['api_key'], $param['phone'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This phone already exist.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //die('------------');

    //----------------------------------

    if ($db->isExistEmailExectCurrentUser($header['api_key'], $param['email'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This email already exist.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //die('------------');

    //----------------------------------

    // if(!empty($input['dpt_id'])){
    //     $param['dpt_id'] = $input['dpt_id'];
    // } else{
    //     $param['dpt_id'] = null;
    // }

    // if(!empty($input['dg_id'])){
    //     $param['dg_id'] = $input['dg_id'];
    // } else{
    //     $param['dg_id'] = null;
    // }

    if (!empty($input['blood_group'])) {
        $param['blood_group'] = $input['blood_group'];
    } else {
        $param['blood_group'] = null;
    }

    $param['api_key'] = $header['api_key'];

    //-----------------------------------

    if ($result = $db->updateUser($param)) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfully updated the account";
        $feedback["results"] = array();

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Cann't updated the account!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }
});

$app->post('/addUserInfo', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    $param = $request->getParams();
    $uid = $db->getUserByKey($header['api_key'])['id'];

    // Handling request params null exception

    if (count($param) == 0) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'No information found.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //--------------------------------

    if ($db->isExistPhoneExceptCurrentUser($header['api_key'], empty($param['phone']))) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This phone already exist.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //----------------------------------

    if ($db->isExistEmailExectCurrentUser($header['api_key'], empty($param['email']))) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'This email already exist.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //-------------------------------------

    $param['uid'] = $uid;
    $photo = null;

    if (!empty($param['p_photo'])) {
        $photo = $param['p_photo'];
        unset($param['p_photo']);
    }

    // var_dump($param);
    // exit;

    //-----------------------------------

    if ($result = $db->addUserInfo($uid, $param)) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfully updated the account";
        $feedback["results"] = array();

        if ($photo != null) {
            $uploaded = $db->uploadPhoto($uid, $photo);

            if (!$uploaded) {
                $feedback['error'] = true;
                $feedback['msg'] = "photo uploaded failed.";
                $feedback["results"] = array();
            }
        }

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Cann't updated the account!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }
});

$app->post('/getUserByKey', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];

    //-----------------------------------

    if ($result = $db->getUserByKey($api_key)) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfully updated the account";
        $feedback["results"] = array();
        $tmp = array();
        $tmp['uid'] = $result['id'];
        $tmp['name'] = $result['name'];
        $tmp['phone'] = $result['phone'] ?? "Unknown";
        $tmp['email'] = $result['email'] ?? "Unknown";
        $tmp['dpt_id'] = $result['dpt_id'];
        $tmp['dpt_name'] = $result['dpt_name'] ?? "Unknown";
        $tmp['dg_id'] = $result['dg_id'];
        $tmp['dg_name'] = $result['dg_name'] ?? "Unknown";
        $tmp['joining_date'] = $result['joining_date'] ?? "Unknown";
        $tmp['blood_group'] = $result['blood_group'] ?? "Unknown";
        $tmp['role_id'] = $result['role_id'];
        $tmp['role_name'] = $result['role_name'];
        $tmp['access_code'] = $result['access_code'];
        $tmp['api_key'] = $result['api_key'];

        array_push($feedback["results"], $tmp);


        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "User not found!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }
});

$app->post('/getUserInfoByKey', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];
    $uid = $db->getUserByKey($api_key)['id'];

    //-----------------------------------

    if ($result = $db->getUserInfoByUid($uid)) {

        // die('--------------------');

        $feedback['error'] = false;
        $feedback['msg'] = "Successfully updated the account";
        $feedback["results"] = array();

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

        array_push($feedback["results"], $tmp);


        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "User info not found!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }
});

$app->post('/getAllUser', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];


    $result = $db->getAllUser($api_key);

    if ($result != null) {
        //die('--------------');

        $feedback['error'] = false;
        $feedback['msg'] = "succeed";
        $feedback['results'] = array();

        while ($task = $result->fetch_assoc()) {
            //die('--------------');
            $tmp = array();
            $tmp['uid'] = $task['id'];
            $tmp['name'] = $task['name'];
            $tmp['phone'] = $task['phone'];
            $tmp['email'] = $task['email'];
            $tmp['dpt_id'] = $task['dpt_id'];
            $tmp['dpt_name'] = $task['dpt_name'];
            $tmp['dg_id'] = $task['dg_id'];
            $tmp['dg_name'] = $task['dg_name'];
            $tmp['joining_date'] = $task['joining_date'];
            $tmp['blood_group'] = $task['blood_group'];
            $tmp['role_id'] = $task['role_id'];
            $tmp['role_name'] = $task['role_name'];
            $tmp['access_code'] = $task['access_code'];
            $tmp['api_key'] = $task['api_key'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/resetPassword', function (Request $request, Response $response, array $args) {

    // check ApiKey null exception
    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withJson(200, $feedback)
            ->write(json_encode($feedback));
    }


    $input = $request->getParams();
    $required_fields = array('new_password');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        }
    }

    $result = $db->updatePassword($header['api_key'], $input['new_password']);

    if ($result) {

        $feedback['error'] = FALSE;
        $feedback['msg'] = "Successfully update password!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "Something is wrong. Please try again!";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }
});

//---------------------Role--------------------

$app->post('/createOrUpdateUserRole', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];

    $input = $request->getParams();
    $required_fields = array('role_name', 'access_code');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        }
    }

    if (!empty($input['id'])) {
        $result = $db->updateUserRole($input['id'], $input['role_name'], $input['access_code']);
    } else {
        $result = $db->createUserRole($input['role_name'], $input['access_code']);
    }



    if ($result) {
        $feedback['error'] = false;
        $feedback['msg'] = "Created user role succesfully.";
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Opps... something is wrong.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getAllUserRole', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];


    $result = $db->getUserrole();

    if ($result != null) {

        $feedback['error'] = false;
        $feedback['msg'] = "succeed";
        $feedback['results'] = array();

        while ($task = $result->fetch_assoc()) {
            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['role_name'] = $task['role_name'];
            $tmp['access_code'] = $task['access_code'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/updateAccessCode', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    //$api_key = $header['api_key'];

    $input = $request->getParams();
    $required_fields = array('access_code');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        }
    }

    $access_code = $input['access_code'];
    $api_key = $header['api_key'];

    $result = $db->changeAccessCode($access_code, $api_key);

    if ($result != null) {

        $feedback['error'] = false;
        $feedback['msg'] = "Update access_code successfuly.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//---------------------Leave--------------------


$app->post('/actionLeaveType', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    $input = $request->getParams();


    if (empty($input['action_code'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) action_code is missing or empty';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $action_code = $param['action_code'] = $input['action_code'];
    }

    if ($action_code == 1) {
        // add 
        $required_fields = array('name', 'balance');
    } else if ($action_code == 2) {
        // update
        $required_fields = array('type_id', 'name', 'balance');
    } else if ($action_code == 3) {
        // delete
        $required_fields = array('type_id');
    } else {

        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Invalid action_code.';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }


    $result = $db->actionLeaveType($param);

    if ($result != null) {

        $feedback['error'] = false;
        $feedback['msg'] = "succeed";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getLeaveType', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];


    $result = $db->getLeaveType();

    if ($result != null) {

        $feedback['error'] = false;
        $feedback['msg'] = "succeed";
        $feedback['results'] = array();

        while ($task = $result->fetch_assoc()) {
            $tmp = array();

            $tmp['type_id'] = $task['type_id'];
            $tmp['name'] = $task['name'];
            $tmp['balance'] = $task['balance'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/applyLeave', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];

    $input = $request->getParams();
    $required_fields = array('apply_date', 'purpose', 'from_date', 'to_date', 'type_id');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $param['uid'] = $db->getUserByKey($api_key)['id'];
    $param['max_type_no'] = $db->getMaxTypeNo($param['type_id'], $param['uid']);

    if ($db->applyLeave($param)) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly applying for leave.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getUserLeaveHistory', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $api_key = $header['api_key'];

    $uid = $db->getUserByKey($api_key)['id'];


    $result = $db->getLeaveHistory($uid);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly searched.";
        $feedback['results'] = array();



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

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getAllLeaveHistory', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $result = $db->getAllLeaveHistory();

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly searched.";
        $feedback['results'] = array();



        while ($task = $result->fetch_assoc()) {
            //die('--------------');

            // apply_date', 'purpose', 'from_date', 'to_date', 'leave_type'
            $tmp = array();
            $tmp['id'] = $task['id'];
            $tmp['apply_date'] = $task['apply_date'];
            $tmp['purpose'] = $task['purpose'];
            $tmp['from_date'] = $task['from_date'];
            $tmp['to_date'] = $task['to_date'];
            $tmp['type_id'] = $task['type_id'] ?? 0;
            $tmp['name'] = $task['name'] ?? "Unknown";
            $tmp['status'] = $task['status'];
            //
            $dg_name = $task['dg_name'] ?? " Unknown ";
            $dg_name = $task['dpt_name'] ?? " Unknown ";

            $tmp['uname'] = $task['uname'] . " - " . $dg_name . "(" . $dg_name . ")";

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "No data found.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/changeLeaveStatus', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('id', 'status');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->changeLeaveStatus($param['id'], $param['status']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//---------------------Department--------------------

$app->post('/getAllDepartment', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $result = $db->getAllDepartment();

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();


        while ($task = $result->fetch_assoc()) {

            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['name'] = $task['name'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/createDepartment', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->createDepartment($param['name']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/updateDepartment', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('id', 'name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->updateDepartment($param['id'], $param['name']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/deleteDepartment', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();

    if (empty($input['id'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) id is missing or empty';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $id = $input['id'];
    }

    $result = $db->deleteDepartment($id);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly deleted.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Oparation Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//-------------------Designation-----------------------

$app->post('/getDesignation', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('dpt_id');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $dpt_id = $input[$field];
        }
    }

    $result = $db->getDesignation($dpt_id);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();


        while ($task = $result->fetch_assoc()) {

            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['name'] = $task['name'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/createDesignation', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('dpt_id', 'name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->createDesignation($param['dpt_id'], $param['name']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/updateDesignation', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('id', 'name');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->updateDesignation($param['id'], $param['name']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/deleteDesignation', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();

    if (empty($input['id'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) id is missing or empty';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $id = $input['id'];
    }

    $result = $db->deleteDesignation($id);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly deleted.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Oparation Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//-------------------Attendance----------------------


$app->post('/addAttendance', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('uid', 'date', 'time', 'location', 'status');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    if ($param['status'] == 1) {

        $todayInsert = $db->getAttendanceByDate($param);
        if ($todayInsert != null) {
            $feedback['error'] = false;
            $feedback['msg'] = "Already checked in.";
            $feedback['results'] = array();

            $result = $db->getAttendanceByDate($param);
            $tmp = array();
            $tmp['in_time'] = $result['in_time'];
            $tmp['in_loc'] = $result['in_loc'];
            $tmp['out_time'] = $result['out_time'];
            $tmp['out_loc'] = $result['out_loc'];
            $tmp['status'] = $result['status'];
            array_push($feedback["results"], $tmp);

            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        }
    }


    if ($db->addAttendance($param)) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();

        $result = $db->getAttendanceByDate($param);
        $tmp = array();
        $tmp['in_time'] = $result['in_time'];
        $tmp['in_loc'] = $result['in_loc'];
        $tmp['out_time'] = $result['out_time'];
        $tmp['out_loc'] = $result['out_loc'];
        $tmp['status'] = $result['status'];

        array_push($feedback["results"], $tmp);
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Attendance Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getAttendanceByDate', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('uid', 'date');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }


    $todayInsert = $db->getAttendanceByDate($param);

    if ($todayInsert != null) {
        $feedback['error'] = false;
        $feedback['msg'] = "Already checked in.";
        $feedback['results'] = array();

        $result = $db->getAttendanceByDate($param);
        $tmp = array();
        $tmp['in_time'] = $result['in_time'];
        $tmp['in_loc'] = $result['in_loc'];
        $tmp['out_time'] = $result['out_time'];
        $tmp['out_loc'] = $result['out_loc'];
        array_push($feedback["results"], $tmp);
    } else {

        $feedback['error'] = false;
        $feedback['msg'] = "Today's record not found.";
        $feedback['results'] = array();
    }


    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/getAttendanceReportByDateRange', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    $input = $request->getParams();
    $required_fields = array('from_date', 'to_date');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $uid = $db->getUserByKey($header['api_key'])['id'];

    $result = $db->getAttendanceReportByDateRange($uid, $param['from_date'], $param['to_date']);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly searched.";
        $feedback['results'] = array();


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

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//-------------------Holiday----------------------

$app->post('/getAllHoliday', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $result = $db->getAllHoliday();

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successfuly updated.";
        $feedback['results'] = array();


        while ($task = $result->fetch_assoc()) {

            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['name'] = $task['name'];
            $tmp['date'] = $task['date'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/addOrUpdateOrDeleteHoliday', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();

    if (empty($input['status'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = 'Required field(s) status is missing or empty';
        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    } else {
        $param['status'] = $status = $input['status'];
    }

    if ($status == 1) {
        // add
        $required_fields = array('name', 'date');
    }
    if ($status == 2) {
        //update
        $required_fields = array('id', 'name', 'date');
    }
    if ($status == 3) {
        // delete
        $required_fields = array('id');
    }


    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }

    $result = $db->addOrUpdateOrDeleteHoliday($param);

    if ($result) {


        $feedback['error'] = false;
        $feedback['msg'] = "Successful action.";
        $feedback['results'] = array();


        while ($task = $result->fetch_assoc()) {
            // die('-------------------');

            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['name'] = $task['name'];
            $tmp['date'] = $task['date'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed action.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//----------------- OfficeTime -------------------

$app->get('/getOfficeTime', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }


    $result = $db->getOfficeTime();

    if ($result) {


        $feedback['error'] = false;
        $feedback['msg'] = "Successful action.";
        $feedback['results'] = array();


        while ($task = $result->fetch_assoc()) {
            // die('-------------------');

            $tmp = array();

            $tmp['id'] = $task['id'];
            $tmp['day_name'] = $task['day_name'];
            $tmp['starting_time'] = $task['starting_time'];
            $tmp['ending_time'] = $task['ending_time'];
            $tmp['status'] = $task['status'];

            array_push($feedback["results"], $tmp);
        }
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed action.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

$app->post('/updateOfficeTime', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $input = $request->getParams();
    $required_fields = array('id', 'starting_time', 'ending_time', 'status');

    // Handling request params null exception
    foreach ($required_fields as $field) {
        if (empty($input[$field])) {
            $feedback['error'] = TRUE;
            $feedback['msg'] = 'Required field(s) ' . $field . ' is missing or empty';
            return $response->withStatus(200)
                ->withHeader('Content-Type', 'application/json')
                ->write(json_encode($feedback));
        } else {
            $param[$field] = $input[$field];
        }
    }


    $result = $db->updateOfficeTime($param);

    if ($result) {

        $feedback['error'] = false;
        $feedback['msg'] = "Successful updated.";
        $feedback['results'] = array();
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "Failed action.";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});

//---------------- check offday ----------------

$app->post('/checkIsAnyOffday', function (Request $request, Response $response) {

    $db = new DbHelper();
    $header = apache_request_headers();

    if (empty($header['api_key']) || !$db->isValidApiKey($header['api_key'])) {
        $feedback['error'] = TRUE;
        $feedback['msg'] = "api_key is missing or invalid.";

        return $response->withStatus(200)
            ->withHeader('Content-Type', 'application/json')
            ->write(json_encode($feedback));
    }

    $apiKey = $header['api_key'];
    $uid = $db->getUserByKey($apiKey)['id'];

    $holiday = $db->checkOffdayInHoliday();
    $weekday = $db->checkOffdayInOfficeTime();
    $leaveday = $db->checkIsUserGetLeave($uid);

    if ($holiday) {
        $feedback['error'] = false;
        $feedback['msg'] = "today holiday";
    } else if ($weekday) {
        $feedback['error'] = false;
        $feedback['msg'] = "today week day";
    } else if ($leaveday) {
        $feedback['error'] = false;
        $feedback['msg'] = "you take leave today";
    } else {
        $feedback['error'] = true;
        $feedback['msg'] = "today no holiday";
    }

    return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($feedback));
});



$app->run();
