import { useCallback, useEffect, useMemo, useState } from "react";
import { fetchOrders } from "./api/ordersApi";
import { Order, OrderSide, OrderStatus, OrderType } from "./types";

type StatusFilter = "ALL" | OrderStatus;
type SideFilter = "ALL" | OrderSide;
type TypeFilter = "ALL" | OrderType;
type SortColumn = "createdAt" | "symbol" | "status";
type SortDirection = "asc" | "desc";

interface SortState {
  column: SortColumn;
  direction: SortDirection;
}

const STATUS_OPTIONS: StatusFilter[] = ["ALL", "NEW", "FILLED", "CANCELLED", "REJECTED"];
const SIDE_OPTIONS: SideFilter[] = ["ALL", "BUY", "SELL"];
const TYPE_OPTIONS: TypeFilter[] = ["ALL", "MARKET", "LIMIT"];

function formatPrice(value: number | null): string {
  if (value === null) {
    return "-";
  }
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD"
  }).format(value);
}

function formatDate(value: string): string {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit"
  }).format(new Date(value));
}

function compareOrders(left: Order, right: Order, sort: SortState): number {
  if (sort.column === "createdAt") {
    const leftTime = new Date(left.createdAt).getTime();
    const rightTime = new Date(right.createdAt).getTime();
    return sort.direction === "asc" ? leftTime - rightTime : rightTime - leftTime;
  }

  if (sort.column === "symbol") {
    const comparison = left.symbol.localeCompare(right.symbol);
    return sort.direction === "asc" ? comparison : -comparison;
  }

  const comparison = left.status.localeCompare(right.status);
  return sort.direction === "asc" ? comparison : -comparison;
}

function statusClass(status: OrderStatus): string {
  return `status-pill status-${status.toLowerCase()}`;
}

export default function App() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [search, setSearch] = useState<string>("");
  const [statusFilter, setStatusFilter] = useState<StatusFilter>("ALL");
  const [sideFilter, setSideFilter] = useState<SideFilter>("ALL");
  const [typeFilter, setTypeFilter] = useState<TypeFilter>("ALL");
  const [sort, setSort] = useState<SortState>({
    column: "createdAt",
    direction: "desc"
  });

  const loadOrders = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchOrders();
      setOrders(data);
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : "Unexpected error while loading orders.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadOrders();
  }, [loadOrders]);

  const summary = useMemo(() => {
    const base: Record<OrderStatus, number> = {
      NEW: 0,
      FILLED: 0,
      CANCELLED: 0,
      REJECTED: 0
    };

    for (const order of orders) {
      base[order.status] += 1;
    }

    return {
      total: orders.length,
      ...base
    };
  }, [orders]);

  const displayedOrders = useMemo(() => {
    const term = search.trim().toLowerCase();

    return orders
        .filter((order) => {
          if (statusFilter !== "ALL" && order.status !== statusFilter) {
            return false;
          }
          if (sideFilter !== "ALL" && order.side !== sideFilter) {
            return false;
          }
          if (typeFilter !== "ALL" && order.type !== typeFilter) {
            return false;
          }

          if (!term) {
            return true;
          }

          return order.symbol.toLowerCase().includes(term) || order.id.toLowerCase().startsWith(term);
        })
        .sort((left, right) => compareOrders(left, right, sort));
  }, [orders, search, statusFilter, sideFilter, typeFilter, sort]);

  const hasNoOrders = !loading && !error && displayedOrders.length === 0;

  const handleSort = (column: SortColumn) => {
    setSort((current) => {
      if (current.column !== column) {
        return { column, direction: "asc" };
      }
      return {
        column,
        direction: current.direction === "asc" ? "desc" : "asc"
      };
    });
  };

  const clearFilters = () => {
    setSearch("");
    setStatusFilter("ALL");
    setSideFilter("ALL");
    setTypeFilter("ALL");
    setSort({ column: "createdAt", direction: "desc" });
  };

  return (
    <div className="page-shell">
      <div className="ambient-layer" aria-hidden="true" />
      <main className="content">
        <header className="hero card rise-in">
          <div>
            <p className="eyebrow">Recruiter Demo Mode</p>
            <h1>Order Desk</h1>
            <p className="subtitle">
              FE-01 delivered: read-focused trading view for monitoring order activity.
            </p>
          </div>
          <div className="hero-actions">
            <button className="ghost-button" onClick={() => void loadOrders()} disabled={loading}>
              {loading ? "Refreshing..." : "Refresh"}
            </button>
            <a className="ghost-button" href="/swagger-ui/index.html" target="_blank" rel="noreferrer">
              Open API Docs
            </a>
          </div>
        </header>

        <section className="summary-grid rise-in">
          <article className="summary-tile card">
            <span>Total</span>
            <strong>{summary.total}</strong>
          </article>
          <article className="summary-tile card">
            <span>NEW</span>
            <strong>{summary.NEW}</strong>
          </article>
          <article className="summary-tile card">
            <span>FILLED</span>
            <strong>{summary.FILLED}</strong>
          </article>
          <article className="summary-tile card">
            <span>CANCELLED</span>
            <strong>{summary.CANCELLED}</strong>
          </article>
        </section>

        <section className="card controls rise-in">
          <label className="field">
            <span>Search symbol or id prefix</span>
            <input
              type="text"
              placeholder="e.g. AAPL or 9d5f"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
            />
          </label>
          <label className="field">
            <span>Status</span>
            <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value as StatusFilter)}>
              {STATUS_OPTIONS.map((status) => (
                <option key={status} value={status}>
                  {status}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Side</span>
            <select value={sideFilter} onChange={(event) => setSideFilter(event.target.value as SideFilter)}>
              {SIDE_OPTIONS.map((side) => (
                <option key={side} value={side}>
                  {side}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Type</span>
            <select value={typeFilter} onChange={(event) => setTypeFilter(event.target.value as TypeFilter)}>
              {TYPE_OPTIONS.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </label>
          <div className="field clear-slot">
            <button className="ghost-button" onClick={clearFilters} type="button">
              Reset filters
            </button>
          </div>
        </section>

        {loading ? (
          <section className="card state-panel rise-in">
            <p className="state-title">Loading orders</p>
            <p>Please wait while data is fetched from the API.</p>
          </section>
        ) : null}

        {error ? (
          <section className="card state-panel error-state rise-in" role="alert">
            <p className="state-title">Unable to load orders</p>
            <p>{error}</p>
            <button className="ghost-button" onClick={() => void loadOrders()} type="button">
              Retry
            </button>
          </section>
        ) : null}

        {hasNoOrders ? (
          <section className="card state-panel rise-in">
            <p className="state-title">No orders yet</p>
            <p>Create orders from the API first, then refresh this view.</p>
            <a className="ghost-button" href="/swagger-ui/index.html" target="_blank" rel="noreferrer">
              Create via Swagger
            </a>
          </section>
        ) : null}

        {!loading && !error && displayedOrders.length > 0 ? (
          <section className="card table-shell rise-in">
            <div className="table-caption">
              <p>Orders ({displayedOrders.length})</p>
              <span>Actions column is reserved for FE-04 and FE-05.</span>
            </div>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>
                      <button type="button" className="sort-button" onClick={() => handleSort("symbol")}>
                        Symbol
                      </button>
                    </th>
                    <th>Side</th>
                    <th>Type</th>
                    <th>Qty</th>
                    <th>Limit</th>
                    <th>
                      <button type="button" className="sort-button" onClick={() => handleSort("status")}>
                        Status
                      </button>
                    </th>
                    <th>
                      <button type="button" className="sort-button" onClick={() => handleSort("createdAt")}>
                        Created
                      </button>
                    </th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {displayedOrders.map((order, index) => (
                    <tr key={order.id} className="row-animate" style={{ animationDelay: `${index * 45}ms` }}>
                      <td>
                        <code title={order.id}>{order.id.slice(0, 8)}...</code>
                      </td>
                      <td>{order.symbol}</td>
                      <td>{order.side}</td>
                      <td>{order.type}</td>
                      <td>{order.quantity}</td>
                      <td>{formatPrice(order.limitPrice)}</td>
                      <td>
                        <span className={statusClass(order.status)}>{order.status}</span>
                      </td>
                      <td>{formatDate(order.createdAt)}</td>
                      <td>
                        <div className="action-cell">
                          <button type="button" disabled title="Planned in FE-04">
                            Cancel
                          </button>
                          <button type="button" disabled title="Planned in FE-05">
                            Fill
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        ) : null}
      </main>
    </div>
  );
}
