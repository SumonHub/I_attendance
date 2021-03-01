<?php

namespace app\Helpers;

class Utils
{

    public static function getResponse($data)
    {
        return array(
            'error' => is_null($data),
            'msg' => is_null($data) ? 'Opps! an error occurred!' : 'Get data succesfully',
            'data' => $data
        );
    }
}
