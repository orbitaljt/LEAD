<?php

	require '../aws/aws-autoloader.php';

	/*
	$s3 = new Aws\S3\S3Client([
			'version' => 'latest',
			'region'  => 'us-standard'
		]);
	*/
		
	$s3Client = new Aws\S3\S3Client([
		'version'     => 'latest',
		'region'      => 'ap-southeast-1',
		'scheme' => 'http',
		'credentials' => [
			'key'    => 'AKIAI5L2JSIGMPXEJI2A',
			'secret' => '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',
		],
	]);

		
	/*	
	$sdk = new Aws\Sdk([
		'region'   => 'us-west-2',
		'version'  => 'latest',
		'S3' => [
			'region' => 'ap-southeast-1'
		]
	]);
	
	$s3Client = $sdk->createS3();
	*/
		
	$result = $s3Client->putObject([
		'Bucket' => 'leadimage',
		'Key'    => 'test_user_id/test.txt',
		'Body'   => 'this is the body!'
	]);	
		
		
	// Download the contents of the object.
	$result = $s3Client->getObject([
		'Bucket' => 'leadimage',
		'Key'    => 'test_user_id/test.txt'
	]);

	echo $result['Body'];

		
?>