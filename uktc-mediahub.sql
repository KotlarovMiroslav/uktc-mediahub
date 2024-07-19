-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 26, 2022 at 08:10 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uktc-mediahub`
--

-- --------------------------------------------------------

--
-- Table structure for table `media`
--

CREATE TABLE `media` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `genre` varchar(25) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `qr_code` text NOT NULL DEFAULT 'Ψ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `media`
--

INSERT INTO `media` (`id`, `name`, `author`, `year`, `genre`, `description`, `quantity`, `qr_code`) VALUES
(8, 'Под игото', 'Иван Вазов', 1234, 'роман', 'Голям роман д сфгфдсйгбнфдиг', 25, 'Ψ'),
(9, 'Още', 'Един', 2020, 'Филм', '-Филм за порастнали', 18, 'Ψ'),
(12, 'test', 'test', 2022, 'asdadad', 'asdasdad', 68, '8588007275437'),
(13, 'no', 'no', 2022, 'sadadsda', 'asdadad', 2, 'Ψ'),
(14, 'duvki', 'ivo', 2022, 'za duvchene', 'pov: ne sa duvki', 19, '8001090970497'),
(15, 'haha', 'haha', 12312313, 'sddasda', 'asdsadad', 12313, 'Ψ'),
(16, 'asddad', 'asdasda', 232, 'sdsaasd', 'asdasdad', 2323, 'Ψ');

-- --------------------------------------------------------

--
-- Table structure for table `rent`
--

CREATE TABLE `rent` (
  `id_rent` int(255) NOT NULL,
  `id_media` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `start_date` varchar(50) DEFAULT NULL,
  `end_date` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rent`
--

INSERT INTO `rent` (`id_rent`, `id_media`, `id_user`, `start_date`, `end_date`) VALUES
(51, 9, 11, '25-05-2022', '28-05-2022'),
(52, 9, 13, '26-05-2022', '28-05-2022'),
(53, 8, 11, '28-04-2022', '28-04-2022'),
(54, 14, 11, '27-06-2022', '28-06-2022'),
(55, 12, 13, '27-06-2022', '28-06-2022');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `course_number` varchar(15) DEFAULT NULL,
  `egn` varchar(15) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `course_number`, `egn`, `phone_number`, `description`) VALUES
(11, 'Иван', '181181', '181818181', '1818181818181', 'асд йкойк'),
(13, 'Росен', '15425', '01524545445', '088546985', 'Отличник');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `media`
--
ALTER TABLE `media`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rent`
--
ALTER TABLE `rent`
  ADD PRIMARY KEY (`id_rent`) USING BTREE,
  ADD KEY `id_media` (`id_media`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `media`
--
ALTER TABLE `media`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `rent`
--
ALTER TABLE `rent`
  MODIFY `id_rent` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rent`
--
ALTER TABLE `rent`
  ADD CONSTRAINT `rent_ibfk_1` FOREIGN KEY (`id_media`) REFERENCES `media` (`id`),
  ADD CONSTRAINT `rent_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
