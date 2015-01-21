<!DOCTYPE html>
<html>
<head>
	<title>GPS Trackker</title>
</head>
<body>
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