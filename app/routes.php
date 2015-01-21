<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::get('auth/login', 'AuthController@create');
Route::get('auth/logout', 'AuthController@destroy');

Route::resource('auth', 'AuthController');

Route::group(array('before' => ['auth']), function() {

	Route::get('/', 'MapsController@index');
	Route::resource('map', 'MapsController');


});