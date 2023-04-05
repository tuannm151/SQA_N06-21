const orderItemListDOM = document.querySelector(".order__items");
const orderDetailsDOM = document.querySelector(".order__detail");
const firstOrderItem = orderItemListDOM.firstElementChild;
console.log(firstOrderItem);
if (firstOrderItem) {
  firstOrderItem.classList.add("active");
}

const formatter = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
  minimumFractionDigits: 2,
});

const getOrderById = async (id) => {
  const response = await fetch(
    `${ctx}/orders?&action=getOrderJson&orderId=${id}`,
    {
      method: "GET",
    }
  );
  return await response.json();
};

const setSpinner = (element) => {
  element.innerHTML =
    '<div class="spinner-wrapper"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>';
};

const setActiveOrderItem = (element) => {
  const orderItemList = document.querySelectorAll(".order__item");
  orderItemList.forEach((item) => {
    item.classList.remove("active");
  });
  element.classList.add("active");
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

orderItemListDOM.addEventListener("click", async (e) => {
  const orderItemDOM = e.target.closest(".order__item");
  if (!orderItemDOM) return;
  const orderId = orderItemDOM.dataset.orderId;

  setActiveOrderItem(orderItemDOM);
  setSpinner(orderDetailsDOM);
  scrollToTop();

  const data = await getOrderById(orderId);
  const itemsCount = data.orderItems.reduce(
    (acc, item) => acc + item.quantity,
    0
  );
  orderDetailsDOM.innerHTML = `<div class="order__detail-header">
            <h3 class="order__detail-title">Order Summary</h3>
            <span class="order__detail-id">#OD${data.id}</span>
          </div>

          <div class="order__detail-content">
          ${data.orderItems
            .map(
              (item) => `
            <div class="order__detail-item">
                <div class="order__detail-img">
                  <img src="${item.product.imageUrl}" alt="A product" />
                </div>
                <div class="order__detail-info">
                  <div class="order__detail-name">
                    <span>${item.product.name}</span>
                  </div>
                  <div class="order__detail-price">
                    <span>${formatter.format(item.price)}</span>
                  </div>
                  <div class="order__detail-quantity">
                    <span>Quantity: ${item.quantity}</span>
                  </div>
                </div>
              </div>
          `
            )
            .join("")}
          </div>
          <div class="order__detail-contact">
            <h3 class="order__detail-title order__detail-title--contact">
              Shipping
            </h3>
            <div class="order__detail-contact-item">
              <i class="bi bi-geo-alt"></i>
              <span class="order__detail-contact-text"
                >${data.address}
              </span>
            </div>
            <div class="order__detail-contact-item">
              <i class="bi bi-telephone"></i>
              <span class="order__detail-contact-text">${data.phone}</span>
            </div>
          </div>
          <div class="checkout__cart-info">
            <span class="checkout__cart-info-item-label"
              >Subtotal (<span id="checkout-cart-count">${itemsCount}</span>)</span
            >
            <span class="checkout__cart-info-item-value">${formatter.format(
              data.totalPrice
            )}</span>
            <span class="checkout__cart-info-item-label">Shipping</span>
            <span class="checkout__cart-info-item-value">$0</span>
            <span class="checkout__cart-info-item-label item-text--total"
              >Total price</span
            >
            <span class="checkout__cart-info-item-value item-text--total"
              >${formatter.format(data.totalPrice)}</span
            >
          </div>`;
});
