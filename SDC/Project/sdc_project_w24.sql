-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Apr 16, 2024 at 04:39 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sdc_project_w24`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `ID` int(255) NOT NULL,
  `Investor_ID` int(255) NOT NULL,
  `Advisor_ID` int(255) NOT NULL,
  `Profile_ID` int(255) NOT NULL,
  `Account_Name` varchar(256) NOT NULL,
  `Reinvest` tinyint(1) NOT NULL,
  `Cash` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `account_stock_info`
--

CREATE TABLE `account_stock_info` (
  `ID` int(11) NOT NULL,
  `Account_ID` int(255) NOT NULL,
  `Stock_ID` int(255) NOT NULL,
  `Stokes` double NOT NULL,
  `ACB` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `company_holdings`
--

CREATE TABLE `company_holdings` (
  `Stock_ID` int(255) NOT NULL,
  `Shares` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `ID` int(255) NOT NULL,
  `Name` varchar(256) NOT NULL,
  `Cash` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `profile_sector_info`
--

CREATE TABLE `profile_sector_info` (
  `ID` int(255) NOT NULL,
  `Profile_ID` int(255) NOT NULL,
  `Sector_ID` int(255) NOT NULL,
  `Percentage` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sector`
--

CREATE TABLE `sector` (
  `ID` int(255) NOT NULL,
  `Name` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE `stock` (
  `ID` int(255) NOT NULL,
  `Symbol` varchar(256) NOT NULL,
  `Company_Name` varchar(256) NOT NULL,
  `Sector_ID` int(255) NOT NULL,
  `Price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stock_history`
--

CREATE TABLE `stock_history` (
  `Time` timestamp NOT NULL DEFAULT current_timestamp(),
  `ID` int(255) NOT NULL,
  `Stock_ID` int(255) NOT NULL,
  `Type` enum('SetPrice','Dividend') NOT NULL,
  `Current_Price` double DEFAULT NULL,
  `New_Price` double DEFAULT NULL,
  `DividendPerSharePrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `ID` int(255) NOT NULL,
  `Time` timestamp NOT NULL DEFAULT current_timestamp(),
  `Type` enum('BUY','SELL','Cash','Company_To_Firm','Firm_To_Investor','Dividend_Cash') NOT NULL,
  `Account_ID` int(255) DEFAULT NULL,
  `Stock_ID` int(255) DEFAULT NULL,
  `Stocks` double DEFAULT NULL,
  `Per_Stock_Price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(255) NOT NULL,
  `Name` varchar(256) NOT NULL,
  `Role` enum('INVESTOR','ADVISOR','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Advisor_ID` (`Advisor_ID`),
  ADD KEY `Investor_ID` (`Investor_ID`),
  ADD KEY `Profile_ID` (`Profile_ID`);

--
-- Indexes for table `account_stock_info`
--
ALTER TABLE `account_stock_info`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `unique_account_stock_combination` (`Account_ID`,`Stock_ID`);

--
-- Indexes for table `company_holdings`
--
ALTER TABLE `company_holdings`
  ADD PRIMARY KEY (`Stock_ID`),
  ADD UNIQUE KEY `unique_stock_stokes` (`Stock_ID`,`Shares`);

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `profile_sector_info`
--
ALTER TABLE `profile_sector_info`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Sector_ID` (`Sector_ID`),
  ADD KEY `profile_sector_info_ibfk_1` (`Profile_ID`);

--
-- Indexes for table `sector`
--
ALTER TABLE `sector`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Sector_ID` (`Sector_ID`);

--
-- Indexes for table `stock_history`
--
ALTER TABLE `stock_history`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Stock_ID` (`Stock_ID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Account_ID` (`Account_ID`),
  ADD KEY `Stock_ID` (`Stock_ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `account_stock_info`
--
ALTER TABLE `account_stock_info`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `profile`
--
ALTER TABLE `profile`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `profile_sector_info`
--
ALTER TABLE `profile_sector_info`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sector`
--
ALTER TABLE `sector`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stock`
--
ALTER TABLE `stock`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stock_history`
--
ALTER TABLE `stock_history`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`Advisor_ID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `account_ibfk_2` FOREIGN KEY (`Investor_ID`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `account_ibfk_3` FOREIGN KEY (`Profile_ID`) REFERENCES `profile` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `account_stock_info`
--
ALTER TABLE `account_stock_info`
  ADD CONSTRAINT `account_stock_info_ibfk_1` FOREIGN KEY (`Account_ID`) REFERENCES `account` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `account_stock_info_ibfk_2` FOREIGN KEY (`Stock_ID`) REFERENCES `stock` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `company_holdings`
--
ALTER TABLE `company_holdings`
  ADD CONSTRAINT `company_holdings_ibfk_1` FOREIGN KEY (`Stock_ID`) REFERENCES `stock` (`ID`);

--
-- Constraints for table `profile_sector_info`
--
ALTER TABLE `profile_sector_info`
  ADD CONSTRAINT `profile_sector_info_ibfk_1` FOREIGN KEY (`Profile_ID`) REFERENCES `profile` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `profile_sector_info_ibfk_2` FOREIGN KEY (`Sector_ID`) REFERENCES `sector` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `stock`
--
ALTER TABLE `stock`
  ADD CONSTRAINT `stock_ibfk_1` FOREIGN KEY (`Sector_ID`) REFERENCES `sector` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `stock_history`
--
ALTER TABLE `stock_history`
  ADD CONSTRAINT `stock_history_ibfk_1` FOREIGN KEY (`Stock_ID`) REFERENCES `stock` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`Account_ID`) REFERENCES `account` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`Stock_ID`) REFERENCES `stock` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
