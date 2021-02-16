<?php

namespace app\Helpers;

class Utils
{

    public static function getResponse(array $data)
    {
        return array(
            'error' => empty($data),
            'msg' => empty($data) ? 'Opps! An error occurred' : 'Get data succesfully',
            'data' => $data
        );
    }
}
