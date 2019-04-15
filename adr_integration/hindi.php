<?php

require_once ("db.php");

// Create connection
$conn = new mysqli($servername, $user, $password, $database);

//To handle hindi
mysqli_query($conn, 'SET character_set_results=utf8');
mysqli_query($conn, 'SET names=utf8');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

/*
  // Read from DB
  $hindi = "SELECT state, constituency, hindi_state, hindi_constituency FROM loksabha_2019 WHERE constituency = 'Andaman and Nicobar Islands'";
  $result = mysqli_query($conn, $hindi) or die('Error:' . mysqli_error($conn));
  echo "Rows: " . mysqli_num_rows($result) . "\n";
  if (mysqli_num_rows($result) > 0) {
  while ($row = mysqli_fetch_assoc($result)) {
  echo "$row[state], $row[constituency], $row[hindi_state], $row[hindi_constituency]\n";
  }
  }

  // Write to DB
  $sql = "UPDATE loksabha_2019 SET hindi_state = \"अण्डमान और निकोबार द्वीपसमूह\", hindi_constituency= \"अण्डमान और निकोबार द्वीपसमूह\" "
  . "WHERE state = 'Andaman and Nicobar Islands' AND constituency = 'Andaman and Nicobar Islands'";
  if (!$sql)
  die('\nError:' . $sql . ' -> ' . mysqli_error($conn));

  echo "-------------------------------------\n";
  $hindi = "SELECT state, constituency, hindi_state, hindi_constituency FROM loksabha_2019 WHERE constituency = 'Andaman and Nicobar Islands'";
  $result = mysqli_query($conn, $hindi) or die('Error:' . mysqli_error($conn));
  if (mysqli_num_rows($result) > 0) {
  while ($row = mysqli_fetch_assoc($result)) {
  echo "$row[state], $row[constituency], $row[hindi_state], $row[hindi_constituency]\n";
  }
  } */

// Read from DB
$hindi = "SELECT name, hindi_state FROM loksabha_2019 WHERE id =44"; //name = 'Kuldeep Rai Sharma' AND constituency = 'Andaman and Nicobar Islands'";
$result = mysqli_query($conn, $hindi) or die('Error:' . mysqli_error($conn));
echo "Rows: " . mysqli_num_rows($result) . "\n";
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        echo "$row[name], $row[hindi_state]\n";
    }
}

// Write to DB
$sql = "UPDATE loksabha_2019 SET hindi_state =\"कुलदीप राय शर्मा\" WHERE id = 44"; //name = 'Kuldeep Rai Sharma' AND constituency = 'Andaman and Nicobar Islands'";
if (!$sql)
    die('\nError:' . $sql . ' -> ' . mysqli_error($conn));
echo "$sql \n\n";

// Read from DB
$hindi = "SELECT name, hindi_state FROM loksabha_2019 WHERE id =44"; //name = 'Kuldeep Rai Sharma' AND constituency = 'Andaman and Nicobar Islands'";
$result = mysqli_query($conn, $hindi) or die('Error:' . mysqli_error($conn));
echo "Rows: " . mysqli_num_rows($result) . "\n";
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        echo "$row[name], $row[hindi_state]\n";
    }
}
