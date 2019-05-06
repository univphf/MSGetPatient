-- --------------------------------------------------------
-- Hôte :                        127.0.0.1
-- Version du serveur:           10.3.13-MariaDB - mariadb.org binary distribution
-- SE du serveur:                Win64
-- HeidiSQL Version:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Export de la structure de la base pour patients
CREATE DATABASE IF NOT EXISTS `patients` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `patients`;

-- Export de la structure de la table patients. pat
CREATE TABLE IF NOT EXISTS `pat` (
  `IPP` int(11) NOT NULL,
  `NOM` varchar(100) DEFAULT NULL,
  `NOM_MAR` varchar(100) DEFAULT NULL,
  `PRENOM` varchar(100) DEFAULT NULL,
  `DDN` date DEFAULT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IPP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table patients.pat : ~2 rows (environ)
/*!40000 ALTER TABLE `pat` DISABLE KEYS */;
INSERT INTO `pat` (`IPP`, `NOM`, `NOM_MAR`, `PRENOM`, `DDN`, `TEL`, `EMAIL`) VALUES
	(1, 'TONDEUR', '', 'HERVE', '1971-01-12', '0695807044', 'tondeur-h@ch-valenciennes.fr'),
	(2, 'TONDEUR', NULL, 'HERVE', '1971-01-12', '0695807044', 'tondeur-h@ch-valenciennes.fr');
/*!40000 ALTER TABLE `pat` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
