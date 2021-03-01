<?php

use Slim\App;
use app\Controllers\UserController;
use app\Controllers\LeaveController;
use app\Controllers\DateTimeController;
use app\Controllers\DepartmentController;
use app\Controllers\DesignationController;

require '../vendor/autoload.php';

$app = new App([
    'settings' => [
        'displayErrorDetails' => true
    ]
]);

$app->get('/', function($request, $response, $args){
    $input = $request->getParams();

});

//============== User =================

$app->get('/login', UserController::class . ':login');
$app->get('/createUser', UserController::class . ':createUser');
$app->get('/updateUser', UserController::class . ':updateUser');
$app->get('/addUserInfo/{uid}', UserController::class . ':addUserInfo');
$app->get('/getUser/[{uid}]', UserController::class . ':getUser');
$app->get('/getUserInfo/[{uid}]', UserController::class . ':getUserInfo');
$app->get('/resetPassword/{uid}', UserController::class . ':resetPassword');
//role
$app->get('/getRole', UserController::class . ':getRole');
$app->get('/addRole', UserController::class . ':addRole');
$app->get('/updateRole', UserController::class . ':updateRole');
$app->get('/deleteRole/{id}', UserController::class . ':deleteRole');
$app->get('/changeUserRole/{uid}', UserController::class . ':changeUserRole');
//attendance
$app->get('/addAttendance', UserController::class . ':addAttendance');
$app->get('/getAttendanceByDate', UserController::class . ':getAttendanceByDate');
$app->get('/getAttendanceReportByDateRange/{uid}', UserController::class . ':getAttendanceReportByDateRange');

//================= Leave ===============

$app->get('/getLeaveType', LeaveController::class . ':getLeaveType');
$app->post('/addLeaveType', LeaveController::class . ':addLeaveType');
$app->post('/updateLeaveType', LeaveController::class . ':updateLeaveType');
$app->delete('/deleteLeaveType/{id}', LeaveController::class . ':deleteLeaveType');

$app->get('/getLeaveHistory/[{uid}]', LeaveController::class . ':getLeaveHistory');
$app->post('/applyLeave', LeaveController::class . ':applyLeave');
$app->get('/changeLeaveStatus/{uid}', LeaveController::class . ':changeLeaveStatus');

//============= Department ==============

$app->get('/getAllDepartment', DepartmentController::class . ':getAllDepartment');
$app->post('/addDepartment', DepartmentController::class . ':addDepartment');
$app->post('/updateDepartment', DepartmentController::class . ':updateDepartment');
$app->delete('/deleteDepartment/{dpt_id}', DepartmentController::class . ':deleteDepartment');

//============ Designation ===============

$app->get('/getDesignation/[{dpt_id}]', DesignationController::class . ':getDesignation');
$app->post('/addDesignation', DesignationController::class . ':addDesignation');
$app->post('/updateDesignation', DesignationController::class . ':updateDesignation');
$app->delete('/deleteDesignation/{dg_id}/{dpt_id}', DesignationController::class . ':deleteDesignation');

//========== DateTime =============

$app->get('/getAllHoliday', DateTimeController::class . ':getAllHoliday');
$app->post('/addHoliday', DateTimeController::class . ':addHoliday');
$app->post('/updateHoliday', DateTimeController::class . ':updateHoliday');
$app->delete('/deleteHoliday/{id}', DateTimeController::class . ':deleteHoliday');
//
$app->get('/getOfficeTime', DateTimeController::class . ':getOfficeTime');
$app->post('/updateOfficeTime', DateTimeController::class . ':updateOfficeTime');

$app->run();
