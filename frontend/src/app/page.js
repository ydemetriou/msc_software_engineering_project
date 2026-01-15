"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import API_BASE_URL from "@/config/api";

export default function Home() {
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(true);

  // 1. Λήψη δεδομένων

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/recipes`)
      .then((res) => {
        // Έλεγχος αν η απάντηση είναι ΟΚ πριν προσπαθήσουμε να διαβάσουμε JSON
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setRecipes(data);
      })
      .catch((err) => {
        console.error("Error fetching recipes:", err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  // 2. Διαγραφή
  const handleDelete = async (id) => {
    if (confirm("Είσαι σίγουρος ότι θες να διαγράψεις αυτή τη συνταγή;")) {
      try {
        await fetch(`${API_BASE_URL}/api/recipes/${id}`, {
          method: "DELETE",
        });
        setRecipes(recipes.filter((recipe) => recipe.id !== id));
      } catch (error) {
        alert("Σφάλμα κατά τη διαγραφή");
      }
    }
  };

  if (loading)
    return <div className="text-center mt-10">Φόρτωση συνταγών...</div>;

  return (
    <div className="min-h-screen p-8 max-w-6xl mx-auto bg-gray-50">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">📖 Οι Συνταγές μου</h1>
        <Link href="/create">
          <button className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition shadow-md">
            + Νέα Συνταγή
          </button>
        </Link>
      </div>

      {/* Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {recipes.map((recipe) => (
          <div
            key={recipe.id}
            className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition duration-300 border border-gray-100"
          >
            {/* --- ΑΛΛΑΓΗ: Εμφάνιση Φωτογραφίας --- */}
            <div className="h-48 bg-gray-200 relative group">
              {recipe.photoUrls && recipe.photoUrls.length > 0 ? (
                <img
                  src={recipe.photoUrls[0]}
                  alt={recipe.name}
                  className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                />
              ) : (
                <div className="w-full h-full flex items-center justify-center text-4xl">
                  🥘
                </div>
              )}
              {/* Overlay με τον χρόνο */}
              <div className="absolute bottom-2 right-2 bg-black bg-opacity-60 text-white text-xs px-2 py-1 rounded">
                ⏱️ {recipe.totalTime}'
              </div>
            </div>
            {/* ------------------------------------ */}

            <div className="p-5">
              <div className="flex justify-between items-start mb-2">
                <h2 className="text-xl font-bold text-gray-800 truncate pr-2">
                  {recipe.name}
                </h2>
                <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full whitespace-nowrap">
                  {recipe.category}
                </span>
              </div>

              <p className="text-sm text-gray-500 mb-4 font-medium">
                📊 Δυσκολία:{" "}
                <span
                  className={
                    (recipe.difficulty || "").toUpperCase() === "EASY"
                      ? "text-green-600"
                      : (recipe.difficulty || "").toUpperCase() === "MEDIUM"
                        ? "text-orange-500"
                        : "text-red-600"
                  }
                >
                  {recipe.difficulty || "Μη ορισμένο"}
                </span>
              </p>

              <div className="flex justify-between items-center mt-4 pt-4 border-t border-gray-100">
                <Link href={`/recipe/${recipe.id}`}>
                  <button className="text-blue-600 hover:text-blue-800 font-bold text-sm">
                    Δείτε περισσότερα →
                  </button>
                </Link>
                <button
                  onClick={() => handleDelete(recipe.id)}
                  className="text-gray-400 hover:text-red-500 transition"
                  title="Διαγραφή"
                >
                  🗑️
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {recipes.length === 0 && (
        <div className="text-center mt-20">
          <p className="text-xl text-gray-400 mb-4">
            Δεν υπάρχουν συνταγές ακόμα.
          </p>
          <Link href="/create">
            <button className="text-blue-600 font-bold hover:underline">
              Ξεκίνα προσθέτοντας μία!
            </button>
          </Link>
        </div>
      )}
    </div>
  );
}
