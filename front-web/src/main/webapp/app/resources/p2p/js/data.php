<?php
	$testData = array("targetName" => "张三的借款",
		"targetAmount" => "20",
		"yearRate" => 20,
		"liftTime" => "4个月",
		"targetRate" => 40,
		"targetOver" => "120,000",
		"targetId" => 1);
	$arr = [$testData,$testData,$testData,$testData,$testData,$testData,$testData];
	echo json_encode($arr)
?>