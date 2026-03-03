import { Order } from "../types";

export async function fetchOrders(): Promise<Order[]> {
  const response = await fetch("/orders", {
    headers: {
      Accept: "application/json"
    }
  });

  if (!response.ok) {
    throw new Error(`Failed to load orders (${response.status})`);
  }

  return (await response.json()) as Order[];
}
