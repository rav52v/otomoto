-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Czas generowania: 12 Gru 2018, 13:11
-- Wersja serwera: 5.7.24-0ubuntu0.18.04.1
-- Wersja PHP: 7.2.10-0ubuntu0.18.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `otomoto5`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `otomoto`
--

CREATE TABLE `otomoto` (
  `id` int(11) NOT NULL,
  `location` varchar(100) NOT NULL DEFAULT '',
  `mobile` bigint(11) DEFAULT NULL,
  `sellerName` varchar(100) DEFAULT NULL,
  `dateOfIssue` varchar(100) DEFAULT NULL,
  `equipment` text,
  `description` text,
  `price` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `offerId` bigint(11) DEFAULT NULL,
  `ofertaOd` varchar(100) DEFAULT NULL,
  `kategoria` varchar(100) DEFAULT NULL,
  `marka` varchar(100) DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `wersja` varchar(100) DEFAULT NULL,
  `rok` int(11) DEFAULT NULL,
  `przebieg` int(11) DEFAULT NULL,
  `pojemnosc` int(11) DEFAULT NULL,
  `vin` varchar(100) DEFAULT NULL,
  `paliwo` varchar(100) DEFAULT NULL,
  `moc` int(11) DEFAULT NULL,
  `skrzynia` varchar(100) DEFAULT NULL,
  `uszkodzony` int(11) DEFAULT '0',
  `naped` varchar(100) NOT NULL,
  `typ` varchar(100) DEFAULT NULL,
  `drzwi` int(11) DEFAULT NULL,
  `miejsca` int(11) DEFAULT NULL,
  `anglik` int(11) DEFAULT '0',
  `kolor` varchar(100) DEFAULT NULL,
  `pierwszaRejestracja` varchar(100) DEFAULT NULL,
  `krajPochodzenia` varchar(100) DEFAULT NULL,
  `numerRej` varchar(100) DEFAULT NULL,
  `pl` int(11) DEFAULT '0',
  `tuning` int(11) DEFAULT '0',
  `aso` int(11) DEFAULT '0',
  `zabytek` int(11) DEFAULT '0',
  `bezwypadkowy` int(11) DEFAULT '0',
  `stan` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `otomoto`
--
ALTER TABLE `otomoto`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `otomoto`
--
ALTER TABLE `otomoto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
