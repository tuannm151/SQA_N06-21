const cartItemsContainer = document.querySelector(".cart__items");
const cartCountDOM = document.getElementById("cartCount");
const subtotalDOM = document.getElementById("subtotal");
const totalDOM = document.getElementById("total");
const totalValueDOM = document.getElementById("total-value");
const checkoutCartCountDOM = document.getElementById("checkout-cart-count");

let subtotalValue = parseFloat(subtotalDOM.dataset.subtotal);

const formatter = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
  minimumFractionDigits: 2,
});

const changeTotalPriceDOM = (price) => {
  subtotalDOM.innerText = formatter.format(price);
  totalValueDOM.innerText = formatter.format(price);
  totalDOM.innerText = formatter.format(price);
};

const changeCartCount = (count) => {
  cartCountDOM.dataset.cartCount = count;
  cartCountDOM.innerText = count;
  checkoutCartCountDOM.innerText = count;
};

const changeQtyValue = (qtyTextDom, qtyDatasetDOM, qty) => {
  qtyDatasetDOM.dataset.qtyValue = qty;
  qtyTextDom.innerText = qty;
};

const changeCartItemTotalValue = (dom, price) => {
  dom.innerText = formatter.format(price);
};

const sendCartItemRequest = (action, productId) => {
  let url = `${ctx}/cart?&action=${action}&productId=${productId}`;
  fetch(url, {
    method: "POST",
  });
};

cartItemsContainer.addEventListener("click", (e) => {
  const buttonDOM = e.target.closest(".btn-action");
  if (!buttonDOM) return;

  const cartItemDOM = buttonDOM.closest(".cart__item");
  const qtyValueDOM = cartItemDOM.querySelector(".cart__item-qty-value");
  const cartItemTotalValueDOM = cartItemDOM.querySelector(
    ".cart__item-total-value"
  );
  const qty = parseInt(cartItemDOM.dataset.qtyValue);
  const cartCount = parseInt(cartCountDOM.dataset.cartCount);
  const productPrice = parseFloat(cartItemDOM.dataset.productPrice);

  const productId = parseInt(cartItemDOM.dataset.productId);

  switch (buttonDOM.dataset.action) {
    case "add":
      changeQtyValue(qtyValueDOM, cartItemDOM, qty + 1);
      changeCartItemTotalValue(cartItemTotalValueDOM, productPrice * (qty + 1));
      changeCartCount(cartCount + 1);
      subtotalValue += productPrice;
      changeTotalPriceDOM(subtotalValue);
      sendCartItemRequest("add", productId);
      break;
    case "reduce":
      if (qty === 1) {
        return;
      }
      changeQtyValue(qtyValueDOM, cartItemDOM, qty - 1);
      changeCartItemTotalValue(cartItemTotalValueDOM, productPrice * (qty - 1));
      changeCartCount(cartCount - 1);
      subtotalValue -= productPrice;
      changeTotalPriceDOM(subtotalValue);
      sendCartItemRequest("reduce", productId);

      break;
    case "delete":
      changeCartCount(cartCount - qty);
      subtotalValue -= productPrice * qty;
      changeTotalPriceDOM(subtotalValue);
      sendCartItemRequest("delete", productId);
      cartItemDOM.remove();
      break;
    default:
      break;
  }
});
