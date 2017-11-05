
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `AdminId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`AdminId`, `UserId`) VALUES
(1, 1),
(2, 10),
(3, 12);

--
-- Table structure for table `badfav`
--

CREATE TABLE `badfav` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `cse`
--

CREATE TABLE `cse` (
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `Title` varchar(150) NOT NULL,
  `Description` varchar(250) NOT NULL,
  `FileName` varchar(50) DEFAULT NULL,
  `ShowFile` varchar(150) DEFAULT NULL,
  `NoticeType` varchar(50) NOT NULL,
  `Approval` int(2) NOT NULL DEFAULT '0',
  `UserId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `csefav`
--

CREATE TABLE `csefav` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Table structure for table `discipline`
--

CREATE TABLE `discipline` (
  `NoticeId` int(11) NOT NULL,
  `Title` varchar(150) NOT NULL,
  `Description` varchar(250) NOT NULL,
  `FileName` varchar(250) DEFAULT NULL,
  `ShowFile` varchar(150) DEFAULT NULL,
  `AdminId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `disciplineadmin`
--

CREATE TABLE `disciplineadmin` (
  `id` int(11) NOT NULL,
  `disciplineName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table `disciplinefav`
--

CREATE TABLE `disciplinefav` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Table structure for table `disciplineseen`
--

CREATE TABLE `disciplineseen` (
  `Id` int(11) NOT NULL,
  `NoticeId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `ece`
--

CREATE TABLE `ece` (
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `Title` varchar(150) NOT NULL,
  `Description` varchar(250) NOT NULL,
  `FileName` varchar(50) DEFAULT NULL,
  `ShowFile` varchar(50) DEFAULT NULL,
  `NoticeType` varchar(50) NOT NULL,
  `Approval` int(2) DEFAULT '0',
  `UserId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Table structure for table `ecefav`
--

CREATE TABLE `ecefav` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `hrm`
--

CREATE TABLE `hrm` (
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `Title` varchar(150) NOT NULL,
  `Description` varchar(250) NOT NULL,
  `FileName` varchar(50) DEFAULT NULL,
  `ShowFile` varchar(50) DEFAULT NULL,
  `NoticeType` varchar(50) NOT NULL,
  `Approval` int(2) DEFAULT '0',
  `UserId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `seencse`
--

CREATE TABLE `seencse` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `seenece`
--

CREATE TABLE `seenece` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `seenvarsity`
--

CREATE TABLE `seenvarsity` (
  `Id` int(11) NOT NULL,
  `NoticeId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `subscription`
--

CREATE TABLE `subscription` (
  `id` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `Batch` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `superadmin`
--

CREATE TABLE `superadmin` (
  `adminId` int(11) NOT NULL,
  `VarsityName` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserId` int(11) NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `Email` varchar(70) NOT NULL,
  `Password` varchar(30) NOT NULL,
  `Discipline` varchar(30) NOT NULL,
  `Batch` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `varsityfav`
--

CREATE TABLE `varsityfav` (
  `Id` int(11) UNSIGNED NOT NULL,
  `NoticeId` int(11) UNSIGNED NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
--
-- Table structure for table `varsitynotice`
--

CREATE TABLE `varsitynotice` (
  `NoticeId` int(11) NOT NULL,
  `Title` varchar(150) NOT NULL,
  `Description` varchar(250) NOT NULL,
  `FileName` varchar(250) DEFAULT NULL,
  `ShowFile` varchar(150) DEFAULT NULL,
  `AdminId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`AdminId`),
  ADD KEY `admin_id` (`AdminId`,`UserId`),
  ADD KEY `User_Id` (`UserId`);

--
-- Indexes for table `badfav`
--
ALTER TABLE `badfav`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `cse`
--
ALTER TABLE `cse`
  ADD PRIMARY KEY (`NoticeId`),
  ADD KEY `UserId` (`UserId`);

--
-- Indexes for table `csefav`
--
ALTER TABLE `csefav`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `discipline`
--
ALTER TABLE `discipline`
  ADD PRIMARY KEY (`NoticeId`),
  ADD KEY `noticeId` (`NoticeId`,`Title`,`Description`,`FileName`,`AdminId`,`Date`),
  ADD KEY `AdminId` (`AdminId`);

--
-- Indexes for table `disciplineadmin`
--
ALTER TABLE `disciplineadmin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`disciplineName`,`password`);

--
-- Indexes for table `disciplinefav`
--
ALTER TABLE `disciplinefav`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `disciplineseen`
--
ALTER TABLE `disciplineseen`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `id` (`Id`,`NoticeId`,`UserId`),
  ADD KEY `NoticeId` (`UserId`),
  ADD KEY `UserId` (`NoticeId`);

--
-- Indexes for table `ece`
--
ALTER TABLE `ece`
  ADD PRIMARY KEY (`NoticeId`),
  ADD KEY `UserId` (`UserId`);

--
-- Indexes for table `ecefav`
--
ALTER TABLE `ecefav`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `hrm`
--
ALTER TABLE `hrm`
  ADD PRIMARY KEY (`NoticeId`),
  ADD KEY `UserId` (`UserId`);

--
-- Indexes for table `seencse`
--
ALTER TABLE `seencse`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `seenece`
--
ALTER TABLE `seenece`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `seenvarsity`
--
ALTER TABLE `seenvarsity`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `id` (`Id`,`NoticeId`,`UserId`),
  ADD KEY `NoticeId` (`NoticeId`),
  ADD KEY `UserId` (`UserId`);

--
-- Indexes for table `subscription`
--
ALTER TABLE `subscription`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`UserId`),
  ADD KEY `userId` (`UserId`),
  ADD KEY `Batch` (`Batch`);

--
-- Indexes for table `superadmin`
--
ALTER TABLE `superadmin`
  ADD PRIMARY KEY (`adminId`),
  ADD KEY `adminId` (`adminId`,`VarsityName`),
  ADD KEY `password` (`password`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserId`),
  ADD KEY `UserId` (`UserId`,`UserName`,`Email`,`Password`,`Discipline`,`Batch`);

--
-- Indexes for table `varsityfav`
--
ALTER TABLE `varsityfav`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `NoticeId` (`NoticeId`);

--
-- Indexes for table `varsitynotice`
--
ALTER TABLE `varsitynotice`
  ADD PRIMARY KEY (`NoticeId`),
  ADD KEY `noticeId` (`NoticeId`,`Title`,`Description`,`FileName`,`AdminId`,`Date`),
  ADD KEY `AdminId` (`AdminId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `AdminId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `badfav`
--
ALTER TABLE `badfav`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cse`
--
ALTER TABLE `cse`
  MODIFY `NoticeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `csefav`
--
ALTER TABLE `csefav`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `discipline`
--
ALTER TABLE `discipline`
  MODIFY `NoticeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `disciplineadmin`
--
ALTER TABLE `disciplineadmin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `disciplinefav`
--
ALTER TABLE `disciplinefav`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `disciplineseen`
--
ALTER TABLE `disciplineseen`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `ece`
--
ALTER TABLE `ece`
  MODIFY `NoticeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `ecefav`
--
ALTER TABLE `ecefav`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `hrm`
--
ALTER TABLE `hrm`
  MODIFY `NoticeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `seencse`
--
ALTER TABLE `seencse`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `seenece`
--
ALTER TABLE `seenece`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `seenvarsity`
--
ALTER TABLE `seenvarsity`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `subscription`
--
ALTER TABLE `subscription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `superadmin`
--
ALTER TABLE `superadmin`
  MODIFY `adminId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `UserId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `varsityfav`
--
ALTER TABLE `varsityfav`
  MODIFY `Id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `varsitynotice`
--
ALTER TABLE `varsitynotice`
  MODIFY `NoticeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `cse`
--
ALTER TABLE `cse`
  ADD CONSTRAINT `cse_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`);

--
-- Constraints for table `discipline`
--
ALTER TABLE `discipline`
  ADD CONSTRAINT `discipline_ibfk_1` FOREIGN KEY (`AdminId`) REFERENCES `disciplineadmin` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `discipline_ibfk_2` FOREIGN KEY (`AdminId`) REFERENCES `disciplineadmin` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `disciplineseen`
--
ALTER TABLE `disciplineseen`
  ADD CONSTRAINT `disciplineseen_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `discipline` (`NoticeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `disciplineseen_ibfk_2` FOREIGN KEY (`NoticeId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ece`
--
ALTER TABLE `ece`
  ADD CONSTRAINT `ece_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`);

--
-- Constraints for table `seencse`
--
ALTER TABLE `seencse`
  ADD CONSTRAINT `seencse_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`),
  ADD CONSTRAINT `seencse_ibfk_2` FOREIGN KEY (`NoticeId`) REFERENCES `cse` (`NoticeId`);

--
-- Constraints for table `seenece`
--
ALTER TABLE `seenece`
  ADD CONSTRAINT `seenece_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`),
  ADD CONSTRAINT `seenece_ibfk_2` FOREIGN KEY (`NoticeId`) REFERENCES `ece` (`NoticeId`);

--
-- Constraints for table `seenvarsity`
--
ALTER TABLE `seenvarsity`
  ADD CONSTRAINT `seenvarsity_ibfk_1` FOREIGN KEY (`NoticeId`) REFERENCES `varsitynotice` (`NoticeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `seenvarsity_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `subscription_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `subscription_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `varsitynotice`
--
ALTER TABLE `varsitynotice`
  ADD CONSTRAINT `varsitynotice_ibfk_1` FOREIGN KEY (`AdminId`) REFERENCES `superadmin` (`adminId`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
