"use client";

import { useState, useEffect, use } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function RecipeDetails({ params: paramsPromise }) {
  const [id, setId] = useState(null);

  useEffect(() => {
    if (paramsPromise instanceof Promise) {
      paramsPromise.then((p) => setId(p.id));
    } else {
      setId(paramsPromise.id);
    }
  }, [paramsPromise]);

  const router = useRouter();
  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    const fetchRecipe = async () => {
      try {
        const res = await fetch(`http://localhost:8081/api/recipes/${id}`);
        if (!res.ok) throw new Error("Recipe not found");
        const data = await res.json();
        setRecipe(data);
      } catch (err) {
        alert("Η συνταγή δεν βρέθηκε!");
        router.push("/");
      } finally {
        setLoading(false);
      }
    };

    fetchRecipe();
  }, [id, router]);

  const handleDelete = async () => {
    if (!confirm("Είσαι σίγουρος ότι θες να διαγράψεις τη συνταγή;")) return;

    try {
      await fetch(`http://localhost:8081/api/recipes/${id}`, {
        method: "DELETE",
      });
      alert("Διαγράφηκε επιτυχώς!");
      router.push("/");
    } catch (err) {
      alert("Σφάλμα κατά τη διαγραφή");
    }
  };

  if (loading || !recipe)
    return <div className="p-10 text-center">Φόρτωση...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto bg-white shadow-xl rounded-xl overflow-hidden">
        {/* Header με Κεντρική Φωτογραφία */}
        <div className="relative h-64 bg-gray-200">
          {recipe.photoUrls && recipe.photoUrls.length > 0 ? (
            <img
              src={recipe.photoUrls[0]}
              alt={recipe.name}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="flex items-center justify-center h-full text-gray-400 text-4xl">
              🍳
            </div>
          )}
          <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black to-transparent p-6">
            <h1 className="text-4xl font-bold text-white">{recipe.name}</h1>
            <div className="text-white opacity-90 mt-2 flex gap-4">
              <span className="bg-blue-600 px-2 py-1 rounded text-sm">
                {recipe.category}
              </span>
              <span className="bg-orange-500 px-2 py-1 rounded text-sm">
                {recipe.difficulty}
              </span>
              <span className="bg-green-600 px-2 py-1 rounded text-sm">
                ⏱️ {recipe.totalTime}'
              </span>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="p-4 bg-gray-100 flex gap-3 border-b">
          <Link
            href="/"
            className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
          >
            ← Πίσω
          </Link>
          <Link
            href={`/edit/${recipe.id}`}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            ✏️ Επεξεργασία
          </Link>
          <button
            onClick={handleDelete}
            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
          >
            🗑️ Διαγραφή
          </button>
          <Link
            href={`/run/${recipe.id}`}
            className="ml-auto px-6 py-2 bg-purple-600 text-white font-bold rounded shadow hover:bg-purple-700"
          >
            ▶️ ΕΚΤΕΛΕΣΗ
          </Link>
        </div>

        <div className="p-8 grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="md:col-span-1 bg-yellow-50 p-6 rounded-lg border border-yellow-100 h-fit">
            <h2 className="text-xl font-bold mb-4 text-yellow-800">
              🛒 Υλικά Συνταγής
            </h2>
            <ul className="space-y-2">
              {recipe.ingredients.map((ing, i) => (
                <li
                  key={i}
                  className="flex justify-between border-b border-yellow-200 pb-1 last:border-0"
                >
                  <span>{ing.name}</span>
                  <span className="font-bold">
                    {ing.quantity}
                    {ing.unit}
                  </span>
                </li>
              ))}
            </ul>
          </div>

          <div className="md:col-span-2 space-y-6">
            <h2 className="text-2xl font-bold text-gray-800">👣 Εκτέλεση</h2>

            {recipe.steps.map((step, i) => (
              <div
                key={i}
                className="border rounded-lg overflow-hidden bg-white shadow-sm hover:shadow-md transition"
              >
                <div className="flex bg-gray-50 border-b p-3 items-center justify-between">
                  <div className="flex items-center gap-3">
                    <span className="bg-blue-600 text-white w-8 h-8 flex items-center justify-center rounded-full font-bold">
                      {i + 1}
                    </span>
                    <h3 className="font-bold text-lg">{step.title}</h3>
                  </div>
                  <span className="text-sm bg-gray-200 px-2 py-1 rounded">
                    ⏱️ {step.duration}'
                  </span>
                </div>

                <div className="p-4">
                  <p className="text-gray-700 mb-4 whitespace-pre-wrap">
                    {step.description}
                  </p>

                  {/* Φωτογραφία Βήματος */}
                  {step.photoUrls && step.photoUrls.length > 0 && (
                    <div className="mb-4">
                      <img
                        src={step.photoUrls[0]}
                        alt={`Step ${i + 1}`}
                        className="rounded-lg max-h-64 object-cover border"
                      />
                    </div>
                  )}

                  {step.ingredients && step.ingredients.length > 0 && (
                    <div className="bg-blue-50 p-3 rounded text-sm">
                      <span className="font-bold text-blue-800 block mb-1">
                        Για αυτό το βήμα χρειάζεσαι:
                      </span>
                      <div className="flex flex-wrap gap-2">
                        {step.ingredients.map((ing, k) => (
                          <span
                            key={k}
                            className="bg-white border border-blue-200 px-2 py-1 rounded text-blue-600"
                          >
                            {ing.name} ({ing.quantity}
                            {ing.unit})
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
