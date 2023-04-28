const formDOM = document.querySelector("#form");
const nameInputDOM = document.querySelector("#name");
const emailInputDOM = document.querySelector("#email");
const passwordInputDOM = document.querySelector("#password");
const rePasswordInputDOM = document.querySelector("#repassword");
const formAlertTextDOM = document.querySelector(".form-alert-text");

const setErrorFor = (input, message) => {
  const fieldGroup = input.parentElement;
  fieldGroup.className = "field-group field-group--invalid";
  const error = fieldGroup.querySelector(".field-group__error");
  error.innerText = message;
};

const checkNameIsValid = (name) => {
  if (name.trim() === "") {
    setErrorFor(nameInputDOM, "Please enter a full name");
    return false;
  }
  return true;
};

const checkEmailIsValid = (email) => {
  if (email.trim() === "") {
    setErrorFor(emailInputDOM, "Please enter an email address");
    return false;
  }
  // check email validity using regex
  const re =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  if (!re.test(String(email).toLowerCase())) {
    setErrorFor(emailInputDOM, "Please enter a valid email");
    return false;
  }
  return true;
};

const checkPasswordIsValid = (password) => {
  if (password.trim() === "") {
    setErrorFor(passwordInputDOM, "Please enter a password");
    return false;
  }
  return true;
};

const checkRePasswordIsValid = (repassword) => {
  if (repassword.trim() === "") {
    setErrorFor(rePasswordInputDOM, "Please enter a password");
    return false;
  }
  if (repassword !== passwordInputDOM.value) {
    setErrorFor(rePasswordInputDOM, "Passwords do not match");
    return false;
  }
  return true;
};

formDOM.addEventListener("submit", (e) => {
  const name = nameInputDOM?.value;
  const email = emailInputDOM.value;
  const password = passwordInputDOM.value;
  const repassword = rePasswordInputDOM?.value;
  console.log(name, email, password, repassword);
  const nameIsValid = name !== undefined ? checkNameIsValid(name) : true;
  const repasswordIsValid =
    repassword !== undefined ? checkRePasswordIsValid(repassword) : true;
  const emailIsValid = checkEmailIsValid(email);
  const passwordIsValid = checkPasswordIsValid(password);
  console.log(nameIsValid, emailIsValid, passwordIsValid, repasswordIsValid);
  if (!nameIsValid || !emailIsValid || !passwordIsValid || !repasswordIsValid) {
    e.preventDefault();
  }
});

nameInputDOM?.addEventListener("input", (e) => {
  if (nameInputDOM.classList.contains("has-value")) {
    return;
  }
  nameInputDOM.className = "field-group__input has-value";
  const fieldGroup = e.target.parentElement;
  fieldGroup.className = "field-group";
  const error = fieldGroup.querySelector(".field-group__error");
  error.innerText = "";
  formAlertTextDOM?.firstChild.remove();
});

emailInputDOM.addEventListener("input", (e) => {
  emailInputDOM.className = "field-group__input has-value";
  const fieldGroup = e.target.parentElement;
  fieldGroup.className = "field-group";
  const error = fieldGroup.querySelector(".field-group__error");
  error.innerText = "";
  formAlertTextDOM?.firstChild.remove();
});
passwordInputDOM.addEventListener("input", (e) => {
  if (passwordInputDOM.classList.contains("has-value")) {
    return;
  }
  passwordInputDOM.className = "field-group__input has-value";
  const fieldGroup = e.target.parentElement;
  fieldGroup.className = "field-group";
  const error = fieldGroup.querySelector(".field-group__error");
  error.innerText = "";
  formAlertTextDOM?.firstChild.remove();
});

rePasswordInputDOM?.addEventListener("input", (e) => {
  if (rePasswordInputDOM.classList.contains("has-value")) {
    return;
  }
  rePasswordInputDOM.className = "field-group__input has-value";
  const fieldGroup = e.target.parentElement;
  fieldGroup.className = "field-group";
  const error = fieldGroup.querySelector(".field-group__error");
  error.innerText = "";
  formAlertTextDOM?.firstChild.remove();
});

nameInputDOM?.addEventListener("blur", (e) => {
  const name = e.target.value.trim();
  if (name.length > 0) {
    return;
  }
  nameInputDOM.className = "field-group__input";
});

emailInputDOM.addEventListener("blur", (e) => {
  const email = e.target.value.trim();
  if (email.length > 0) {
    return;
  }
  emailInputDOM.className = "field-group__input";
});

passwordInputDOM.addEventListener("blur", (e) => {
  const password = e.target.value.trim();
  if (password.length > 0) {
    return;
  }
  passwordInputDOM.className = "field-group__input";
});

rePasswordInputDOM?.addEventListener("blur", (e) => {
  const repassword = e.target.value.trim();
  if (repassword.length > 0) {
    return;
  }
  rePasswordInputDOM.className = "field-group__input";
});
