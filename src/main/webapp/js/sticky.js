const header = document.getElementById("header");

const sticky = header.offsetTop;

window.onscroll = function () {
  changeHeaderStatus();
};

function changeHeaderStatus() {
  if (window.pageYOffset > sticky) {
    header.classList.add("sticky");
  } else {
    header.classList.remove("sticky");
  }
}

const hamburger = document.querySelector(".hamburger");
const navMenu = document.querySelector(".nav");

hamburger.addEventListener("click", mobileMenu);

function mobileMenu() {
  console.log("clicked");
  hamburger.classList.toggle("active");
  navMenu.classList.toggle("active");
}
