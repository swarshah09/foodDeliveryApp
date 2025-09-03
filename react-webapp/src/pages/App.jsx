import React, { useEffect, useMemo, useState } from 'react'
import axios from 'axios'
import { clsx } from 'clsx'

const api = axios.create({ baseURL: '/api' })

function Card({ title, children, className }) {
  return (
    <div className={clsx('rounded-2xl bg-white/70 backdrop-blur shadow-lg p-6 border border-white/40', className)}>
      <h2 className="text-xl font-semibold text-gray-800 mb-4">{title}</h2>
      {children}
    </div>
  )
}

export default function App() {
  const [products, setProducts] = useState([])
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({ name: '', price: 0, stock: 0 })
  const [orderForm, setOrderForm] = useState({ productName: '', quantity: 1 })
  const [toast, setToast] = useState(null)

  const showToast = (msg, type = 'success') => {
    setToast({ msg, type })
    setTimeout(() => setToast(null), 2200)
  }

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [p, o] = await Promise.all([
        api.get('/product-service/api/products'),
        api.get('/order-service/api/orders')
      ])
      setProducts(p.data)
      setOrders(o.data)
    } catch (e) {
      console.error(e)
      showToast('Failed to fetch data', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchAll()
  }, [])

  const createProduct = async () => {
    try {
      await api.post('/product-service/api/products', {
        name: form.name,
        price: Number(form.price),
        stock: Number(form.stock)
      })
      setForm({ name: '', price: 0, stock: 0 })
      await fetchAll()
      showToast('Product created')
    } catch (e) {
      showToast('Failed to create product', 'error')
    }
  }

  const createOrder = async () => {
    try {
      await api.post('/order-service/api/orders', {
        productName: orderForm.productName,
        quantity: Number(orderForm.quantity)
      })
      setOrderForm({ productName: '', quantity: 1 })
      await fetchAll()
      showToast('Order placed')
    } catch (e) {
      showToast('Failed to place order', 'error')
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-brand-50 via-white to-brand-100">
      <header className="max-w-7xl mx-auto px-6 py-8">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-brand-600 shadow-inner shadow-brand-900/20" />
            <div>
              <h1 className="text-2xl font-bold text-gray-900">ShopSphere</h1>
              <p className="text-sm text-gray-500">Spring Cloud • Kafka • MySQL • React</p>
            </div>
          </div>
          <button onClick={fetchAll} className="px-4 py-2 rounded-xl bg-brand-600 text-white shadow hover:bg-brand-700 transition">{loading ? 'Refreshing…' : 'Refresh'}</button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 pb-16 grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card title="Create Product" className="lg:col-span-1">
          <div className="space-y-3">
            <input className="w-full border rounded-xl px-3 py-2" placeholder="Name" value={form.name} onChange={e => setForm(v => ({ ...v, name: e.target.value }))} />
            <div className="grid grid-cols-2 gap-3">
              <input className="w-full border rounded-xl px-3 py-2" type="number" placeholder="Price" value={form.price} onChange={e => setForm(v => ({ ...v, price: e.target.value }))} />
              <input className="w-full border rounded-xl px-3 py-2" type="number" placeholder="Stock" value={form.stock} onChange={e => setForm(v => ({ ...v, stock: e.target.value }))} />
            </div>
            <button onClick={createProduct} className="w-full px-4 py-2 rounded-xl bg-gray-900 text-white hover:bg-black transition">Add Product</button>
          </div>
        </Card>

        <Card title="Products" className="lg:col-span-2">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="text-gray-500 text-sm">
                  <th className="py-2">Name</th>
                  <th className="py-2">Price</th>
                  <th className="py-2">Stock</th>
                </tr>
              </thead>
              <tbody>
                {products.map(p => (
                  <tr key={p.id} className="border-t">
                    <td className="py-2">{p.name}</td>
                    <td className="py-2">${p.price?.toFixed?.(2) ?? p.price}</td>
                    <td className="py-2">{p.stock}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>

        <Card title="Place Order" className="lg:col-span-1">
          <div className="space-y-3">
            <input className="w-full border rounded-xl px-3 py-2" placeholder="Product name" value={orderForm.productName} onChange={e => setOrderForm(v => ({ ...v, productName: e.target.value }))} />
            <input className="w-full border rounded-xl px-3 py-2" type="number" placeholder="Quantity" value={orderForm.quantity} onChange={e => setOrderForm(v => ({ ...v, quantity: e.target.value }))} />
            <button onClick={createOrder} className="w-full px-4 py-2 rounded-xl bg-brand-600 text-white hover:bg-brand-700 transition">Submit Order</button>
          </div>
        </Card>

        <Card title="Orders" className="lg:col-span-2">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="text-gray-500 text-sm">
                  <th className="py-2">ID</th>
                  <th className="py-2">Product</th>
                  <th className="py-2">Qty</th>
                  <th className="py-2">Created</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(o => (
                  <tr key={o.id} className="border-t">
                    <td className="py-2">{o.id}</td>
                    <td className="py-2">{o.productName}</td>
                    <td className="py-2">{o.quantity}</td>
                    <td className="py-2">{new Date(o.createdAt).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      </main>

      {toast && (
        <div className={clsx('fixed bottom-6 left-1/2 -translate-x-1/2 px-4 py-2 rounded-xl shadow text-white', toast.type === 'success' ? 'bg-emerald-600' : 'bg-rose-600')}>
          {toast.msg}
        </div>
      )}
    </div>
  )
}

