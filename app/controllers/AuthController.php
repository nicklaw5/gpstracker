<?php

class AuthController extends \BaseController {

	function __construct() {}

	/**
	 * Display a listing of the resource.
	 *
	 * @return Response
	 */
	public function index()
	{
		//
	}


	/**
	 * Show the form for creating a new resource.
	 *
	 * @return Response
	 */
	public function create()
	{
		return View::make('auth.login');
	}

	public function postLogin() {
	}

	/**
	 * Attempt to sign the user in
	 *
	 * @return Response
	 */
	public function store()
	{
		if(Auth::attempt(['username' => Input::get('username'),'password'=> Input::get('password')]))
		{	
			// Successful Mobile Login
			if(Request::ajax())
    		{
    			return 'success';
    		}

    		// Successful Web Login
    		return Redirect::action('MapsController@index');
		}

		// Unsuccessful Mobile Login
		if(Request::ajax()) 
		{
			return 'Invalid username or password.';
		}

		// Unsuccessful Mobile Login
		Session::flash('error', 'Invalid email address or password.');
		return Redirect::back()->withInput();			
	}


	/**
	 * Display the specified resource.
	 *
	 * @param  int  $id
	 * @return Response
	 */
	public function show($id)
	{
		//
	}


	/**
	 * Show the form for editing the specified resource.
	 *
	 * @param  int  $id
	 * @return Response
	 */
	public function edit($id)
	{
		//
	}


	/**
	 * Update the specified resource in storage.
	 *
	 * @param  int  $id
	 * @return Response
	 */
	public function update($id)
	{
		//
	}


	/**
	 * Remove the specified resource from storage.
	 *
	 * @param  int  $id
	 * @return Response
	 */
	public function destroy()
	{
		Auth::logout();

		if(Request::ajax())
		{
			return 'success';
		}

		return Redirect::route('auth/logout');
	}

}
