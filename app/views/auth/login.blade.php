<!DOCTYPE html>
<html>
<head>
	<title>GPS Trackker</title>
</head>
<body>

@if(Session::has('error'))
	<p>{{ Session::get('error') }}</p>
@endif

{{ Form::open(['action' => 'AuthController@store']) }}
	<label for="username">Username:</label>
	{{ Form::text('username', null) }}
	<br><br>
	<label for="password">Password:</label>
	{{ Form::password('password') }}
	<br><br>
	{{ Form::submit('Login') }}
{{ Form::close()}}

</body>
</html>