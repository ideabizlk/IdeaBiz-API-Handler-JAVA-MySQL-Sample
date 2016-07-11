CREATE TABLE IF NOT EXISTS `oauth2` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(255) NOT NULL,
  `refreshToken` varchar(255) NOT NULL,
  `consumerKey` varchar(255) NOT NULL,
  `consumerSecret` varchar(255) NOT NULL,
  `scope` varchar(20) NOT NULL,
  `expire` int(10) NOT NULL,
  `tokenURL` text NOT NULL,
  `lastUpdated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;