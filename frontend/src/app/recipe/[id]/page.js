"use client";

import { useState, useEffect, use } from "react";
import Link from "next/link";
import API_BASE_URL from "@/config/api";

export default function RecipeDetails({ params: paramsPromise }) {
  const params = use(paramsPromise);
  const { id } = params;

  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) return;

    fetch(`${API_BASE_URL}/api/recipes/${id}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setRecipe(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching recipe details:", err);
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl text-gray-600 animate-pulse">
          Φόρτωση συνταγής...
        </div>
      </div>
    );

  if (error)
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl text-red-600">Σφάλμα: {error}</div>
      </div>
    );

  if (!recipe) return null;

  // Βοηθητική συνάρτηση για μορφοποίηση χρόνου
  const formatTime = (minutes) => {
    if (!minutes) return "0 λεπτά";
    const hrs = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hrs > 0) {
      return `${hrs}ώ ${mins > 0 ? `${mins}λ` : ""}`;
    }
    return `${mins} λεπτά`;
  };

  return (
    <div className="min-h-screen bg-gray-50 pb-12">
      {/* Hero Section με Κεντρική Εικόνα */}
      <div className="relative h-96 bg-gray-300">
        {recipe.photoUrls && recipe.photoUrls.length > 0 ? (
          <img
            src={recipe.photoUrls[0]}
            alt={recipe.name}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-6xl">
            🥘
          </div>
        )}
        {/* Overlay Gradient */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent"></div>

        {/* Τίτλος και Κατηγορία πάνω στην εικόνα */}
        <div className="absolute bottom-0 left-0 p-8 text-white">
          <span className="bg-green-600 text-xs px-3 py-1 rounded-full uppercase tracking-wider font-semibold">
            {recipe.category}
          </span>
          <h1 className="text-4xl font-bold mt-3 mb-2">{recipe.name}</h1>
          <div className="flex items-center space-x-4 text-sm font-medium">
            <span className="flex items-center">
              ⏱️ Συνολικός Χρόνος: {formatTime(recipe.totalTime)}
            </span>
            <span className="flex items-center">
              📊 Δυσκολία: {recipe.difficulty || "Μη ορισμένη"}
            </span>
          </div>
        </div>
      </div>

      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 mt-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
          {/* Αριστερή Στήλη: Υλικά και Actions */}
          <div className="lg:col-span-1 space-y-8">
            {/* Κουμπιά Ενεργειών */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col gap-3">
              <Link href={`/run/${recipe.id}`} className="w-full">
                <button className="w-full bg-green-600 text-white text-lg font-bold py-3 px-6 rounded-xl hover:bg-green-700 transition transform hover:scale-[1.02] shadow-md flex items-center justify-center">
                  ▶️ Ξεκίνα την εκτέλεση
                </button>
              </Link>
              <div className="flex gap-3">
                <Link href={`/edit/${recipe.id}`} className="flex-1">
                  <button className="w-full bg-blue-100 text-blue-700 font-semibold py-2 px-4 rounded-lg hover:bg-blue-200 transition flex items-center justify-center">
                    ✏️ Επεξεργασία
                  </button>
                </Link>
                <Link href="/" className="flex-1">
                  <button className="w-full bg-gray-100 text-gray-700 font-semibold py-2 px-4 rounded-lg hover:bg-gray-200 transition flex items-center justify-center">
                    🏠 Αρχική
                  </button>
                </Link>
              </div>
            </div>

            {/* Λίστα Υλικών */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
              <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                🛒 Υλικά
              </h2>
              <ul className="space-y-3">
                {recipe.ingredients.map((ing, index) => (
                  <li
                    key={index}
                    className="flex justify-between items-center p-3 bg-gray-50 rounded-lg border border-gray-100"
                  >
                    <span className="font-medium text-gray-700">
                      {ing.name}
                    </span>
                    <span className="font-bold text-green-700 bg-green-50 px-3 py-1 rounded-full">
                      {ing.quantity} {ing.unit.toLowerCase()}
                    </span>
                  </li>
                ))}
              </ul>
            </div>
          </div>

          {/* Δεξιά Στήλη: Βήματα Εκτέλεσης */}
          <div className="lg:col-span-2">
            <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100">
              <h2 className="text-2xl font-bold text-gray-800 mb-8 flex items-center">
                👨‍🍳 Εκτέλεση
              </h2>
              <div className="space-y-8">
                {recipe.steps.map((step, index) => (
                  // ---  Προσθήκη border-b για διαχωρισμό ---
                  <div
                    key={index}
                    className="flex items-start border-b border-gray-100 pb-6 last:border-0 last:pb-0"
                  >
                    {/* Αριθμός Βήματος */}
                    <div className="flex-shrink-0 bg-green-100 text-green-800 font-bold rounded-full w-10 h-10 flex items-center justify-center mr-5 shadow-sm">
                      {index + 1}
                    </div>

                    {/* Περιεχόμενο Βήματος (Κείμενο) - flex-grow για να πιάσει χώρο */}
                    <div className="flex-grow mr-4">
                      <h3 className="text-xl font-bold text-gray-800 mb-2">
                        {step.title}
                      </h3>
                      <p className="text-gray-600 leading-relaxed mb-4">
                        {step.description}
                      </p>
                      {/* Metadata βήματος (Χρόνος & Υλικά) */}
                      <div className="flex flex-wrap gap-3">
                        {step.duration > 0 && (
                          <span className="inline-flex items-center bg-amber-50 text-amber-700 text-sm px-3 py-1 rounded-full font-medium">
                            ⏱️ {formatTime(step.duration)}
                          </span>
                        )}
                        {step.ingredients && step.ingredients.length > 0 && (
                          <span
                            className="inline-flex items-center bg-blue-50 text-blue-700 text-sm px-3 py-1 rounded-full font-medium"
                            title={step.ingredients
                              .map((i) => i.name)
                              .join(", ")}
                          >
                            🥣 {step.ingredients.length} υλικά
                          </span>
                        )}
                      </div>
                    </div>

                    {/* ---  Μικρογραφία Εικόνας Βήματος (μόνο η πρώτη) --- */}
                    {step.photoUrls &&
                      step.photoUrls.length > 0 &&
                      step.photoUrls[0] && (
                        <div className="flex-shrink-0">
                          <img
                            src={step.photoUrls[0]}
                            alt={`Βήμα ${index + 1}`}
                            className="w-28 h-28 object-cover rounded-xl border border-gray-200 shadow-sm"
                          />
                        </div>
                      )}
                    {/* ------------------------------------------------------------- */}
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
