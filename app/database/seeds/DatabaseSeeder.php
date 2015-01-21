<?php

class DatabaseSeeder extends Seeder {

	/**
	 * Run the database seeds.
	 *
	 * @return void
	 */
	public function run()
	{
		Eloquent::unguard();

		$this->call('UsersTableSeeder');
	}
}

class UsersTableSeeder extends Seeder {

    public function run() {

    	$now = date('Y-m-d H:i:s');

        $users = [	
			[
				'username' 			=>	'demo',
	            'password'			=> 	Hash::make('demo'),
	            'created_at'		=> 	$now,
	            'updated_at'		=> 	$now
	        ]
	    ];

        DB::table('users')->insert($users);
        
    }

}
