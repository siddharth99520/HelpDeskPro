-- Update all user passwords with a proper BCrypt hash of "password"
-- Previous hash was manually typed and didn't actually match via BCrypt verification
UPDATE users SET password = '$2a$10$oSdAzelpsPd/y0zDbbJ/QeltrpADdbUmCzah.sQj0p0qPC7glPNF.';
