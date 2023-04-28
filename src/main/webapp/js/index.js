const cartCount = document.querySelector("#cartCount");
const productsContainerDOM = document.querySelector(".products-container");
productsContainerDOM.addEventListener("click", (e) => {
  let productId = null;
  const parentElement = e.target.parentNode;

  if (parentElement.classList.contains("btn-add")) {
    productId = parentElement.parentElement.dataset.productid;
  } else if (parentElement.classList.contains("product__info")) {
    productId = parentElement.dataset.productid;
  }

  if (productId === null) {
    return;
  }

  const currentCartCount = parseInt(cartCount.dataset.cartCount) + 1;
  cartCount.dataset.cartCount = currentCartCount;
  cartCount.innerText = currentCartCount;
  fetch(`${ctx}/cart?&action=add&productId=${productId}`, {
    method: "POST",
  });
});
