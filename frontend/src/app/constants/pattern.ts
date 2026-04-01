export const PHONE_PATTERNS = {
  // Numero italiano: 06 1234567 | 02-1234567 | 333 1234567 | +39 333 1234567
  IT: /^(\+39)?[\s\-]?(\(0\d{1,4}\)|0\d{1,4})?[\s\-]?\d{6,10}$/,
  // Numero mobile italiano: 3XX XXXXXXX (senza prefisso fisso)
  IT_MOBILE: /^(\+39)?[\s\-]?3\d{2}[\s\-]?\d{6,7}$/,
};
